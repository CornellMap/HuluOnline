with open('./origin/namelist.txt') as openfileobject:
	f = open("namelist.txt", "w")
	i = 0
	for line in openfileobject:
		if i % 11 == 2 or i % 11 == 7:
			word = line[4:-6]
			f.write(''.join(c for c in word.lower() if c.islower()) + '\n')
		i += 1
