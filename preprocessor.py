import pandas as pd
pd.options.mode.chained_assignment = None 
import numpy as np
import re
import nltk
from gensim.models import word2vec
from sklearn.manifold import TSNE
import matplotlib.pyplot as plt
import matplotlib.pyplot as plt
from keras.preprocessing.text import text_to_word_sequence
from keras.preprocessing.text import Tokenizer
import multiprocessing # for concurrency 
from gensim.models import Word2Vec
from time import time 
import pandas as pd
import re

def remove_comments(text):
    return re.sub(re.compile('#.*?\n'), '', text)
code = '#pac kage c om.on ed atap oint.con fig;import co m.on edata point.re po.* ; publ ic cl ass Conf ig { priv ate stat ic Config instance = null ; private Ques tionR eposi tory quest ionRepo sitory = null ;pri vate Me dica tio nRep osi tory med icatio nRepo sitory = null ; private Res ponse Reposit ory respon seReposi tory = null ; /*** Use Config.getInstance() */ private Config() {questionRepository = new QuestionRepositoryImpl() ; responseRepository = new ResponseRepositoryImpl();medicationRepository = new MedicationRepositoryImpl() ; } public static Config getInstance() { if ( Config.instance == null ) {Config.instance = new Config(); } return Config.instance ; } public QuestionRepository getQuestionRepository() { return questionRepository ; } public ResponseRepository getResponseRepository() { return responseRepository ; } public MedicationRepository getMedicationRepository() { return medicationRepository ; } } '
#remove_comments(code) 
text_to_word_sequence(code, lower=True, split=' ')
text_to_word_sequence(code, filters='\t\n', lower=True, split=' ')
#t = Tokenizer(num_words=None, filters='\t\n', lower=True, split=' ', char_level=False)
docs = [code]
print ( "line 26", docs)
#t.fit_on_texts(docs)
#print ('Number docs', t.document_count)
#print(t.word_index)
#print(t.word_docs)
#encoded_docs = texts_to_matrix(docs, mode='count')
#print(encoded_docs)


cores = multiprocessing.cpu_count()
# specifies word2vec params
w2v_model = Word2Vec(min_count=20, window=2, size=300, sample=6e-5, alpha=0.03, min_alpha=0.0007,  negative=20, workers=cores-1)
# building vocabulary 
t = time()
w2v_model.build_vocab(docs, progress_per=10000)
print('Time to build vocab: {} mins'.format(round((time() - t) / 60, 2)))
print ( "line 42", docs)
# training word2vec model
t = time()
w2v_model.train(docs, total_examples=w2v_model.corpus_count, epochs=30, report_delay=1) # epochs is number of iterations over corpus 
print('Time to train the model: {} mins'.format(round((time() - t) / 60, 2)))
print ( "line 47", docs)
# init_sims make memory more effecient if called it indicates that there is no further training 
#w2v_model.init_sims(replace=True)


def tsne_plot(model):
    "Creates and TSNE model and plots it"
    labels = []
    tokens = []
    for docs in model.wv.vocab:
        tokens.append(model[docs])
     
        for e_word in docs:
            labels.append(e_word)
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

tsne_plot(w2v_model)
