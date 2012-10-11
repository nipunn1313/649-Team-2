SOURCE=$(shell find jSimPack simulator -name '*.java')
CLASSES=$(subst .java,.class,$(SOURCE))

.PHONY: all code portfolio clean portfolioclean

all: code

code: $(CLASSES)

portfolio: portfolioclean
	mkdir -p ../portfolio/elevatorcontrol
	git diff --name-only OriginalCode | \
		grep "^code/simulator/elevatorcontrol/.*\.java" | \
		sed -e "s/^code\///" | \
		xargs -I {} cp {} ../portfolio/elevatorcontrol/
	./generate_package.sh ../portfolio/elevatorcontrol/package.html

clean: portfolioclean
	find . -name '*'.class -exec rm -f {} ';'

portfolioclean:
	rm -f ../portfolio/elevatorcontrol/*

$(CLASSES): %.class: %.java
	javac -source 1.5 -O -Xlint:all -cp $(PWD) $<

# ensure the next line is always the last line in this file.
# vi:noet
