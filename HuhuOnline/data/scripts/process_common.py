with open('./origin/common.txt') as openfileobject:
	f = open("common.txt", "w")
	i = 0
	for line in openfileobject:
		if i % 2 == 1:
			count = 0
			flag = 0
			word = ""
			for c in line:
				if c == '>':
					count += 1
				if flag == 1:
					if c == '<':
						break
					word += c
				if count == 4:
					flag = 1
			wd = ''.join(c for c in word.lower() if c.islower())
			if len(wd) > 0:
				f.write(wd + '\n')
				print word
		i += 1
