import argparse
import bs4

def wrangle(html):
	"""
	This is a Python version of the "wrangle" static method
	of the Wrangler class in dwim. It's reimplemented here
	for ease of debugging and to have a reference model.

	We take "html", a string containing HTML, and return a
	Python dictionary representing what we've learned from
	that HTML. In the Android version, we'd be returning a
	JSON object.
	"""
	result = {}
	soup = bs4.BeautifulSoup(html, 'lxml')
	print 'soup ok'
	return result

def handle(filename):
	print '==', filename
	soup = bs4.BeautifulSoup(open(filename,'r'), 'xml')
	source = soup.source.string
	expect = soup.expect.string

	result = wrangle(source)

	print result

def main():
	parser = argparse.ArgumentParser(description="prototype test for Dwim wrangler")
	parser.add_argument('filenames', metavar='FILENAME', nargs='+',
			type=str, help='filenames of XML test description')
	args = parser.parse_args()

	for filename in args.filenames:
		handle(filename)

if __name__=='__main__':
	main()
