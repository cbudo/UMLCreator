def main():

	in1 = open("awt_names.txt", "r")
	in2 = open("swing_names.txt", "r")
	out = open("out.txt", "w")
	for line in in1:
		s = line.split(" ")[1]
		out.write("java.awt." + s)

	for line in in2:
		s = line.split(" ")[1]
		out.write("javax.swing." + s) 

	in1.close()
	in2.close()
	out.close()


if __name__ == "__main__":
	main()