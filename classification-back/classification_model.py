from io import BytesIO
from PIL import Image
import base64
import tensorflow as tf
import numpy as np
import cv2
from tensorflow.keras.models import load_model # type: ignore


classification_model = load_model('models/imageclassifier.h5')

def classify(base64_image:str) -> np.ndarray:
    img = readb64(base64_image)
    resize = tf.image.resize(img, (256,256))
    prediction = classification_model.predict(np.expand_dims(resize/255, 0)) # type: ignore
    return prediction

def readb64(base64_image:str): 
    sbuf = BytesIO()
    sbuf.write(base64.b64decode(base64_image)) #type: ignore
    pimg = Image.open(sbuf)  #type: ignore
    return cv2.cvtColor(np.array(pimg), cv2.COLOR_RGB2BGR)  #type: ignore

