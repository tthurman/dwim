import argparse
import bs4
import os

FILE_HEADER = """ /* Generated code; do not edit */

package org.marnanel.dwim.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.marnanel.dwim.test.WranglerTestRoutines.*;

public class WranglerTest {
"""

FILE_FOOTER = """
}"""

METHOD = """
     @Test
     public void %(methodName)s() {

	 WranglerTestRoutines.wranglerTest(
%(source)s,

	///////////////////////////////

%(expect)s
	 );
     }

"""

def wrapString(s):

	def escape(t):
		t = t. \
			replace('\\', '\\\\'). \
			replace('"', '\\"'). \
			replace('\t', '\\t')
		return '"'+t+'"'

	result = '+\n'.join([
		escape(x)
		for x in s.split('\n')
	])

	return result

def handle(filename):

	soup = bs4.BeautifulSoup(open(filename,'r'), 'xml')
	source = soup.source.string
	expect = soup.expect.string

	methodName = os.path.basename(filename).replace('.xml', '').replace('-','_').lower()

	print METHOD % {
	    'source': wrapString(soup.source.string),
	    'expect': wrapString(soup.expect.string),
	    'methodName': methodName,
	}


def main():

	parser = argparse.ArgumentParser(description="build Java source from wrangler test XML files")
	parser.add_argument('filenames', metavar='FILENAME', nargs='+',
			type=str, help='filenames of XML test description')
	args = parser.parse_args()

	print FILE_HEADER

	for filename in args.filenames:
		handle(filename)

	print FILE_FOOTER

if __name__=='__main__':
    main()
