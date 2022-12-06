from cv2 import os
from flask import Flask, request
from numpy import ndarray
from classification_model import *
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

@app.route("/classify", methods=['POST'])
def classify_image():
    request_data = request.json
    if(request_data == None):
        return '{"message": "Invalid Request"}'
    base64_image = request_data.get("image_src")
    if(base64_image == None):
        return '{"message": "Send a image please!"}'

    classify_result:ndarray = classify(base64_image)
    trunc_result = "{0:.3}".format(classify_result.item(0))
    result = trunc_result.replace('[', '').replace(']', '')
    return '{"result":' + result + '}'

if __name__ == '__main__':
    port = os.getenv('PORT')
    if(port == None):
        print("Error on $PORT env variable")
    else:
        app.run(host="0.0.0.0", port=int(port), debug=True)
