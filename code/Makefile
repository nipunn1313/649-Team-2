SOURCE=$(shell find jSimPack simulator -name '*.java')
CLASSES=$(subst .java,.class,$(SOURCE))

.PHONY: all code portfolio clean

all: code

code: $(CLASSES)

portfolio:
	git diff --name-only Project2 | \
		grep "^code/simulator/elevatorcontrol/.*\.java" | \
		sed -e "s/^code\///" | \
		xargs -I {} cp {} ../portfolio/elevatorcontrol/

clean:
	find . -name '*'.class -exec rm -f {} ';'
	rm -f ../portfolio/elevatorcontrol/*.java

$(CLASSES): %.class: %.java
	javac -source 1.5 -O -Xlint:all -cp $(PWD) $<

# ensure the next line is always the last line in this file.
# vi:noet
