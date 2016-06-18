import argparse
import bs4
import json

def wrangle_entries(entries):
	result = []
	for entry in entries:
	    current = {}

            # Pick up all we can from the classes on
            # the entry wrapper div. This tells us
            # a whole lot.
            for klass in entry['class']:
		if '-' in klass \
		    and not klass.startswith('has-') \
		    and not klass.startswith('entry-'):

			last_hyphen = klass.rindex('-')   
			field = klass[:last_hyphen]
			value = klass[last_hyphen+1:]

			current[field] = value

            content = entry.find(class_='entry-content')
	    current['content'] = content.renderContents()

            title = entry.find(class_='entry-title').find('a')
	    current['permalink'] = title['href']
	    current['title'] = title['title']

	    result.append(current)
	return result

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

	entries = soup.find_all(class_='entry-wrapper')

	if entries:
	    result['type'] = 'timeline'
	    result['entries'] = wrangle_entries(entries)
	else:
	    result['type'] = 'unknown'
	return result

def handle(filename):
	print '==', filename
	soup = bs4.BeautifulSoup(open(filename,'r'), 'xml')
	source = soup.source.string
	expect = soup.expect.string

	result = wrangle(source)

	print json.dumps(result, indent=2, sort_keys=True)

def main():
	parser = argparse.ArgumentParser(description="prototype test for Dwim wrangler")
	parser.add_argument('filenames', metavar='FILENAME', nargs='+',
			type=str, help='filenames of XML test description')
	args = parser.parse_args()

	for filename in args.filenames:
		handle(filename)

if __name__=='__main__':
	main()
