import javalang
import re
import nltk
import numpy as np
from gensim.models.fasttext import FastText
import matplotlib.pyplot as plt 
import seaborn as sns 
sns.set_style("darkgrid")
from sklearn.decomposition import PCA
from sklearn.manifold import TSNE
from matplotlib import pyplot

java_code = open('data/dataset.java','r', encoding='utf-8')
fragments = java_code.readlines()
fragremnt= [fragment.lower() for fragment in fragments]
java_code.close()



def tokenizer(fragments):
    for framgent in fragments:
        tokens = list(javalang.tokenizer.tokenize(framgent))
        print(tokens)
    return(tokens_list)


wpt = nltk.WordPunctTokenizer()
tokenized_corpus = [wpt.tokenize(document) for document in fragments]
feature_size = 100    # Word vector dimensionality
window_context = 50          # Context window size
min_word_count = 5   # Minimum word count
sample = 1e-3   # Downsample setting for frequent words

ft_model = FastText(tokenized_corpus, size=feature_size, window=window_context, 
                    min_count=min_word_count,sample=sample, sg=1, iter=50)

similar_words = {search_term: [item[0] for item in ft_model.wv.most_similar([search_term], topn=5)]
                  for search_term in ['class']}
print(similar_words)
print(ft_model.wv['print'])
print(ft_model.wv.similarity(w1='class', w2='interface'))
def tsne_plot(ft_model):
    "Creates and TSNE model and plots it"
    labels = []
    tokens = []
    for word in ft_model.wv.vocab:
        tokens.append(ft_model[word])
        labels.append(word)
    tsne_model = TSNE(perplexity=40, n_components=2, init='pca', n_iter=2500, random_state=23)
    new_values = tsne_model.fit_transform(tokens)
    x = []
    y = []
    for value in new_values:
        x.append(value[0])
        y.append(value[1])       
    plt.figure(figsize=(16, 16)) 
    for i in range(len(x)):
        plt.scatter(x[i],y[i])
        plt.annotate(labels[i],
                     xy=(x[i], y[i]),
                     xytext=(5, 2),
                     textcoords='offset points',
                     ha='right',
                     va='bottom')
    plt.show()

tsne_plot(ft_model)
def closest_words_tsne_plot(ft_model, word):
    arr = np.empty((1,100), dtype='f')
    word_labels = [word]
    # get close words
    close_words = ft_model.similar_by_word(word)
    # add the vector for each of the closest words to the array
    arr = np.append(arr, np.array([ft_model[word]]), axis=0)
    for wrd_score in close_words:
        wrd_vector = ft_model[wrd_score[0]]
        word_labels.append(wrd_score[0])
        arr = np.append(arr, np.array([wrd_vector]), axis=0)
    # find tsne coords for 2 dimensions
    tsne = TSNE(n_components=2, random_state=0)
    np.set_printoptions(suppress=True)
    Y = tsne.fit_transform(arr)
    x_coords = Y[:, 0]
    y_coords = Y[:, 1]
    # display scatter plot
    plt.scatter(x_coords, y_coords)
    for label, x, y in zip(word_labels, x_coords, y_coords):
        plt.annotate(label, xy=(x, y), xytext=(0, 0), textcoords='offset points')
    plt.xlim(x_coords.min() + 0.00005, x_coords.max() + 0.00005)
    plt.ylim(y_coords.min() + 0.00005, y_coords.max() + 0.00005)
    plt.show()

closest_words_tsne_plot(ft_model, 'print')
