with open('./origin/placename.txt') as openfileobject:
	f = open("placename.txt", "w")
	i = 0
	for line in openfileobject:
		if i % 4 == 1:
			word = line[4:-6]
			f.write(''.join(c for c in word.lower() if c.islower()) + '\n')
		i += 1
