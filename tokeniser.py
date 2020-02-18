import tokenize
import multiprocessing # for concurrency 
from gensim.models import Word2Vec # word2vec model
from gensim.models.phrases import Phrases, Phraser # gensim phrases for bigram 
import matplotlib.pyplot as plt # for plot representation
import seaborn as sns # for visualization
sns.set_style("darkgrid")
from sklearn.decomposition import PCA
from sklearn.manifold import TSNE

with tokenize.open('preprocessor.py') as f:
    tokens = tokenize.generate_tokens(f.readline)
    for token in tokens:
        print(str(token).lower().split('.'))

tokenized = str(token)

fragments = [row.split() for row in tokenized]
model = Word2Vec(fragments, min_count=1)
words = list(model.wv.vocab)

#print(model.wv.most_similar(positive=["print"]))

def tsne_plot(model):
    "Creates and TSNE model and plots it"
    labels = []
    tokens = []
    for word in model.wv.vocab:
        tokens.append(model[word])
        labels.append(word)
        print(word)
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

tsne_plot(model)