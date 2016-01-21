import os

def main():


	# f = open("commandline_args.txt", "w")
	# path = "src\\Lab4-2-Singleton\\src\\headfirst\\singleton"
	# package = "headfirst.singleton"
	# for dir in os.listdir(path):
	# 	for file in os.listdir(path + "\\" + dir):
	# 		s = package + "." + dir + "." + file
	# 		s = s.replace(".java", "")
	# 		f.write(s + " ")


	#CHOCOLATE FACTORY
	# f = open("commandline_args.txt", "w")
	# path = "src\\Lab4-2-Singleton\\src\\headfirst\\singleton"
	# package = "headfirst.singleton"
	# for dir in os.listdir(path):
	# 	for file in os.listdir(path + "\\" + dir):
	# 		s = package + "." + dir + "." + file
	# 		s = s.replace(".java", "")
	# 		f.write(s + " ")


	path = "src"
	f = open("commandline_args.txt", "w")
	for outerdir in os.listdir(path):
		if ("pizza" in outerdir) or ("Temp" in outerdir) or ("Lab4-2" in outerdir):
			continue

		for DirOrFile in os.listdir(path + "\\" + outerdir):
			if os.path.isfile(path + "\\" + outerdir + "\\" + DirOrFile):
				s = outerdir + "\\" + DirOrFile
				s = s.replace(".java", "")
				s = s.replace("\\", ".")
				f.write(s + " ")

			else:
				for f1 in os.listdir(path + "\\" + outerdir + "\\" + DirOrFile):
					s = outerdir + "\\" + DirOrFile + "\\" + f1
					s = s.replace(".java", "")
					s = s.replace("\\", ".")
					f.write(s + " ")

if __name__ == "__main__":
	main()