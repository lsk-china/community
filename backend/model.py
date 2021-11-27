class User:
	def __init__(self):
		pass
	def setName(self,name):
		self.name = name
	def getName(self):
		return self.name
	def setPassword(self,password):
		self.password = password
	def getPassword(self):
		return self.password
	def setScore(self,score):
		self.score = score
	def getScore(self):
		return self.score
	def setID(self,id):
		self.id = id
	def getID(self):
		return self.id
	def setPermission(self,permission):
		self.permission = permission
	def getPermission(self):
		return self.permission
	def setMail(self, mail):
		self.mail = mail
	def getMail(self):
		return self.mail

class Permission:
	def __init__(self,p,name):
		self.p = p
		self.name = name
	def getP(self):
		return self.p
	def hasPermission(self, permission):
		return self.p >= permission.getP()
	def __str__(self):
		return self.name

ADMIN = Permission(3, "ADMIN")
MANAGER = Permission(2, "MANAGER")
USER = Permission(1, "USER")
GUEST = Permission(0, "GUEST")

def convertStringtoPermission(string):
	if string == "ADMIN":
		return ADMIN
	elif string == "MANAGER":
		return MANAGER
	elif string == "USER":
		return USER
	else:
		return GUEST

def convertDBUsertoUser(dbUser):
	user = User()
	user.setID(dbUser[0])
	user.setName(dbUser[1])
	user.setPermission(convertStringtoPermission(dbUser[2]))
	user.setPassword(dbUser[3])
	user.setScore(dbUser[4])
	user.setMail(dbUser[5])
	return user

def convertJsonToUser(json):
	user = User()
	user.setID(json["id"])
	user.setName(json["name"])
	user.setPermission(convertStringtoPermission(json["permission"]))
	user.setMail(json["mail"])
	user.setScore(json["score"])
	user.setPassword(["password"])

def convertUsertoJson(user):
	result = {}
	result["name"] = user.getName()
	result["id"] = user.getID()
	result["permission"] = str(user.getPermission())
	result["mail"] = user.getMail()
	result["score"] = user.getScore()
	result["password"] = user.getPassword()
	return result