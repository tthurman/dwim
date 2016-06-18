import bs4

def handle(filename):
	soup = bs4.BeautifulSoup(open(filename,'r'), 'xml')

	print soup.source.string

def main():
	handle('read-page.xml')

if __name__=='__main__':
    main()
