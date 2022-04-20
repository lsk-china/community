from flask import Flask, request
from flask_cors import CORS

app = Flask(__name__)
CORS(app, supports_credentials=True)

@app.route("/userinfo")
def userinfo():
    return {
        "code": 200,
        "message": "Success",
        "data": {
            "username": "admin",
            "id": 1,
            "avatar": "jpg"
        }
    }

if __name__ == "__main__":
    app.run(port=8200)
