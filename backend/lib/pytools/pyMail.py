from lib.pytools.pyBases import *
from lib.pytools.pyLog import Logger

import smtplib
from email.mime.text import MIMEText
from email.header import Header

connectionData = {}
log = Logger("Mail Sender")
connected = false

def connect(host,username,password):
	global connected, connectionData
	connected = true
	connectionData["host"] = host
	connectionData["username"] = username
	connectionData["password"] = password

def send(text,fromAddr,toAddr,subject,encoding="utf-8"):
	msg = MIMEText(text,"plain",encoding)
	msg["From"] = Header(fromAddr,encoding)
	msg["To"] = Header(toAddr,encoding)
	msg["Subject"] = Header(subject,encoding)
	if not connected:
		log.error("Cannot send email: Not connected")
		return
	try:
		smtp = smtplib.SMTP()
		smtp.connect(connectionData["host"], 25)
		smtp.login(connectionData["username"], connectionData["password"])
		smtp.sendmail(fromAddr,toAddr,msg.as_string())
		smtp.close()
	except Exception as e:
		log.error("Cannot send email: ",e)
