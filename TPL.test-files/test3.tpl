# TPL test program 3
# Expected output:
#
# TPL Inerpreter by Tim Brailsford
#
# The square of 12 is 144
# TPL finished OK [20 lines processed]

	integer value
	integer value2
	
	let value = 12
	calculate value2 = value * value

	print "The square of "
	print value
	print " is "
	println value2

END