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

	    images = content.find_all('img')
	    current['images'] = [unicode(img.href) for img in content.find_all('img')]

            title = entry.find(class_='entry-title').find('a')
	    current['permalink'] = title['href']
	    current['title'] = title['title']

	    current['tags'] = []
	    taglist = entry.find(class_='tag')
	    if taglist is not None:
	       for tag in taglist.find_all(rel='tag'):
		   current['tags'].append(tag.string)
            current['tags'] = sorted(current['tags'])

	    current['userpic'] = {}
	    userpic = entry.find(class_='userpic')
	    if userpic is not None:
		img = userpic.find('img')

		if img is not None:
		    for attrib in ('src', 'width', 'height'):
			if img.has_attr(attrib):
			    current['userpic'][attrib] = img[attrib]

	    result.append(current)

	return result

def wrangle_taglist(html):
	result = []

	# scan the <a> links, not the text, so that
	# we get the full name of hierarchical tags.
	links = html.find_all(rel="tag")

	for link in links:
	    target = link['href']

	    last_slash = target.rindex('/')
	    target = target[last_slash+1:]

	    result.append(unicode(target))

	return sorted(result)

def wrangle_icons(iconlist):
	result = []

	for iconinfo in iconlist:

	    # We actually wanted the parent, but
	    # its class is "icon", which is going to
	    # cause clashes sooner or later.
	    icon = iconinfo.parent

	    current = {
		'comment': '',
		'default': False,
		'description': '',
		'height': 0,
		'keywords': [],
		'src': '',
		'title': '',
		'width': 0,
		}

	    current['default'] = 'icon-default' in icon['class']

	    for (field, value) in icon.find('img').attrs.items():
		if field in ('height', 'width', 'title', 'src'):
                   current[field] = value

            for field in ('comment', 'description'):
		span = icon.find(class_='icon-'+field)

		if span is None:
		    continue

		spanchildren = span.children

		# skip the first entry, which is the name of the field
		spanchildren.next()

		text = ''.join([unicode(x) for x in spanchildren]).strip()
		current[field] = text


	    keywords = icon.find(class_='icon-keywords')
	    if keywords is not None:
		for keyword in keywords.find_all('li'):
			current['keywords'].append(unicode(keyword.string))

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

	# XXX check for whether we're logged in

	result = {}
	soup = bs4.BeautifulSoup(html, 'lxml')

	entries = soup.find_all(class_='entry-wrapper')

	if entries:
	    result['type'] = 'timeline'
	    result['entries'] = wrangle_entries(entries)
	    # XXX we also need to add title, subtitle, and whether this is lastn
	    return result

	taglist = soup.find(class_='ljtaglist')
	if taglist is not None:
	    result['type'] = 'taglist'
	    result['tags'] = wrangle_taglist(taglist)
	    return result

	icons = soup.find_all(class_='icon-info')
	if icons:
	    result['type'] = 'icons'
	    result['icons'] = wrangle_icons(icons)
	    return result


	# XXX type="profile"

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
