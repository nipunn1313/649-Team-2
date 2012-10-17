SOURCE=$(shell find jSimPack simulator -name '*.java')
CLASSES=$(subst .java,.class,$(SOURCE))

.PHONY: all code portfolio portfolioclean test testclean check clean 

all: code

code: $(CLASSES)

portfolio: portfolioclean
	mkdir -p ../portfolio/elevatorcontrol
	git diff --name-only OriginalCode | \
		grep "^code/simulator/elevatorcontrol/.*\.java" | \
		sed -e "s/^code\///" | \
		xargs -I {} cp {} ../portfolio/elevatorcontrol/
	./generate_package.sh ../portfolio/elevatorcontrol/package.html

portfolioclean:
	rm -f ../portfolio/elevatorcontrol/*

test: code
	cd ../portfolio/unit_test;        ../../code/run_units.sh        ../../code ../../code
	cd ../portfolio/integration_test; ../../code/run_integrations.sh ../../code ../../code

testclean:
	rm -rf unit_results* integration_results*

check:
	./check_handin.sh

clean: portfolioclean testclean
	find . -name '*'.class -exec rm -f {} ';'

$(CLASSES): %.class: %.java
	javac -source 1.5 -O -Xlint:all -cp $(PWD) $<

# ensure the next line is always the last line in this file.
# vi:noet
