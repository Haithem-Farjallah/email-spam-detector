from flask import Flask, request, jsonify
import os
import joblib
import string
import spacy
from dotenv import load_dotenv
load_dotenv()


API_KEY = os.getenv("API_KEY")

app = Flask(__name__)
nlp = spacy.load("en_core_web_sm")

model = joblib.load("checkpoints/spam_detection_model.pkl")
vectorizer = joblib.load("checkpoints/count_vectorizer.pkl")

def clean_text(s): 
    for cs in s:
        if cs not in string.ascii_letters:
            s = s.replace(cs, ' ')
    return s.rstrip('\r\n')

def remove_little(s): 
    return ' '.join([w for w in s.split() if len(w) > 2])

def lemmatize_text(text):
    doc = nlp(text)
    return " ".join([token.lemma_ for token in doc])

def preprocess(text):
    return lemmatize_text(remove_little(clean_text(text)))



@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json()
    email = data.get("email")

    if not email:
        return jsonify({"error": "Missing 'email' field"}), 400

    processed = preprocess(email)
    prediction = model.predict(vectorizer.transform([processed]))[0]

    result = "Spam" if prediction == 1 else "Not Spam"
    return jsonify({"result": result})

if __name__ == '__main__':
    app.run(port=5000)
