import pyrebase
from aiproject.settings import API_KEY, AUTH_DOMAIN, DATABASE_URL, PROJECT_ID, STORAGE_BUCKET, MESSAGING_SENDER_ID, APP_ID
import firebase_admin
from firebase_admin import credentials
import os

config = {
    "apiKey": API_KEY,
    "authDomain": AUTH_DOMAIN,
    "databaseURL": DATABASE_URL,
    "projectId": PROJECT_ID,
    "storageBucket": STORAGE_BUCKET,
    "messagingSenderId": MESSAGING_SENDER_ID,
    "appId": APP_ID
}

cred = credentials.Certificate(os.path.join(
    os.getcwd(), 'aiproject', 'serviceAccountKey.json'))
firebase_admin.initialize_app(cred, {
    "storageBucket": STORAGE_BUCKET
})

firebase = pyrebase.initialize_app(config)
auth = firebase.auth()
database = firebase.database()
storage = firebase.storage()
