import collections
import os
import random
import urllib
import zipfile

import numpy as np
import tensorflow as tf

learning_rate = 0.1
batch_size = 128
num_steps = 30000
display_step = 1000
eval_step = 10000

eval_words = ['nine', 'of', 'going', 'hardware', 'american', 'britain']

embedding_size = 200
max_vocabulary_size = 50000
min_occurrence = 10
skip_window = 3
num_skips = 2
num_sampled = 64

data_path = 'text8.zip'
with zipfile.ZipFile(data_path) as f:
    text_words = f.read(f.namelist()[0]).lower().split()

len(text_words)

count = [('UNK', -1)]
count.extend(collections.Counter(text_words).most_common(max_vocabulary_size - 1))

for i in range(len(count) - 1, -1, -1):
    if count[i][1] < min_occurrence:
        count.pop(i)
    else:
        break

vocabulary_size = len(count)
word2id = dict()
for i, (word, _) in enumerate(count):
    word2id[word] = i

data = list()
unk_count = 0
for word in text_words:
    index = word2id.get(word, 0)
    if index == 0:
        unk_count += 1
    data.append(index)
count[0] = ('UNK', unk_count)
id2word = dict(zip(word2id.values(), word2id.keys()))

print("Words count", len(text_words))
print("Unique words", len(set(text_words)))
print("Vocabulary size", vocabulary_size)
print("Most common words", count[:10])

data_index = 0


def next_batch(batch_size, num_skips, skip_window):
    global data_index
    assert batch_size % num_skips == 0
    assert num_skips <= 2 * skip_window
    batch = np.ndarray(shape=batch_size, dtype=np.int32)
    labels = np.ndarray(shape=(batch_size, 1), dtype=np.int32)
    span = 2 * skip_window + 1
    buffer = collections.deque(maxlen=span)
    if data_index + span > len(data):
        data_index = 0
    buffer.extend(data[data_index: data_index + span])
    data_index += span
    for i in range(batch_size // num_skips):
        context_words = [w for w in range(span) if w != skip_window]
        words_to_use = random.sample(context_words, num_skips)
        for j, context_words in enumerate(words_to_use):
            batch[i * num_skips + j] = buffer[skip_window]
            labels[i * num_skips + j, 0] = buffer[context_words]
        if data_index == len(data):
            buffer.extend(data[0:span])
            data_index = span
        else:
            buffer.append(data[data_index])
            data_index += 1

    data_index = (data_index + len(data) - span) % len(data)
    return batch, labels


with tf.device('/cpu:0'):
    embedding = tf.Variable(tf.random.normal([vocabulary_size, embedding_size]))
    nce_weights = tf.Variable(tf.random.normal([vocabulary_size, embedding_size]))
    nce_biases = tf.Variable(tf.zeros([vocabulary_size]))


def get_embedding(x):
    with tf.device('/cpu:0'):
        x_embed = tf.nn.embedding_lookup(embedding, x)
        return x_embed


def nce_loss(x_embed, y):
    with tf.device('/cpu:0'):
        y = tf.cast(y, tf.int64)
        loss = tf.reduce_mean(
            tf.nn.nce_loss(weights=nce_weights,
                           biases=nce_biases,
                           labels=y,
                           inputs=x_embed,
                           num_sampled=num_sampled,
                           num_classes=vocabulary_size)
        )

        return loss


def evaluate(x_embed):
    with tf.device('/cpu:0'):
        x_embed = tf.cast(x_embed, tf.float32)
        x_embed_norm = x_embed / tf.sqrt(tf.reduce_sum(tf.square(x_embed)))
        embedding_norm = embedding / tf.sqrt(tf.reduce_sum(tf.square(embedding), 1, keepdims=True), tf.float32)
        cosine_sim_op = tf.matmul(x_embed_norm, embedding_norm, transpose_b=True)
        return cosine_sim_op


optimizer = tf.optimizers.SGD(learning_rate)


def run_optimization(x, y):
    with tf.device('/cpu:0'):
        with tf.GradientTape() as g:
            emb = get_embedding(x)
            loss = nce_loss(emb, y)

        gradients = g.gradient(loss, [embedding, nce_weights, nce_biases])

        optimizer.apply_gradients(zip(gradients, [embedding, nce_weights, nce_biases]))


x_test = np.array([word2id[w.encode('utf-8')] for w in eval_words])


for step in range(1, num_steps + 1):
    batch_x, batch_y = next_batch(batch_size, num_skips, skip_window)
    run_optimization(batch_x, batch_y)
    #print(step)

    if step % display_step == 0 or step == 1:
        loss = nce_loss(get_embedding(batch_x), batch_y)
        print("step %i, loss: %f" % (step, loss))

    if step % eval_step == 0 or step == 1:
        print("Evaluating...")
        sim = (get_embedding(x_test)).numpy()
        for i in range(len(eval_words)):
            top_k = 8
            nearest = (-sim[i, :]).argsort()[1:top_k + 1]
            log_str = '"%s" nearest neighbors' % eval_words[i]
            for k in range(top_k):
                log_str = '%s %s,' % (log_str, id2word[nearest[k]])
            print(log_str)
