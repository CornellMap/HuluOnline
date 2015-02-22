with open('./origin/allnoun.txt') as openfileobject:
	f = open("allnoun.txt", "w")
	i = 0
	for line in openfileobject:
		wordlist = line.split(' ')
		for word in wordlist:
			wd = ''.join(c for c in word.lower() if c.islower())
			if len(wd) > 0:
				f.write(wd + '\n')
