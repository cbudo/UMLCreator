import os

def main():

	# path = "../src"
	# totalClasses = 0
	# f = open("ProjectClasses.txt", "w")
	# for outerdir in os.listdir(path):
	# 	if ("TestingProjects" in outerdir):
	# 		continue

	# 	atBottom = False
	# 	toCheck = outerdir
	# 	currentPath = os.listdir(path + toCheck)
	# 	while not atBottom:
	# 			atBottom = True
	# 			newToCheck = []
	# 			for dirOrFile in currentPath:
	# 				if os.path.isfile(currentPath):
	# 					s = toCheck 


		# while not atBottom:
		# 	atBottom = True
		# 	newToCheck = []
		# 	for DirOrFile in toCheck:
		# 		if os.path.isfile(src + "\\" + toCheck + "\\" + DirOrFile):
		# 			s = outerdir + "\\" + DirOrFile
		# 			s = s.replace(".java", "")
		# 			s = s.replace("\\", ".")
		# 			f.write(s + " ")
		# 			totalClasses += 1

		# 		else:
		# 			newPath = toCheck + "\\"  + DirOrFile
		# 			newToCheck.append(newPath)
		# 			atBottom = False
		# 	toCheck = newToCheck

	print(totalClasses)

if __name__ == "__main__":
	main()