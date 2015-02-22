with open('./origin/sense.txt') as openfileobject:
	f = open("sense.txt", "w")
	i = 0
	for line in openfileobject:
		wordlist = line.split('%')
		f.write(''.join(c for c in wordlist[0].lower() if c.islower()) + '\n')
