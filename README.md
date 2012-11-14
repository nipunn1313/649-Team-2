649-Team-2
==========

Elevator project for 18-649.

<html>
<body>
<h1> 18-649 Project 11</h1>
</center>
<center> <span style="font-weight: bold;">Check the <a
 href="http://www.ece.cmu.edu/%7Eece649/">course webpage</a> for due
dates</span><b><br>
</b>
</center>
<p>Please submit all project-related correspondence to
<img src="../e-mail_addr.gif"><br>
</p>
<hr style="width: 100%; height: 2px;">Changelog:<br>
<ul>
  <li>Dated entries will be added here if the project is modified after
it is released.</li>
</ul>
<hr style="width: 100%; height: 2px;">
<h1>Assignment:</h1>
At this point, you have completed most of the design and testing of
your fast elevator.&nbsp; In this project you will complete testing on
the updated
elevator. You will also use runtime monitoring to verify that
your design meets high level requirements.<br>
<h2>1.&nbsp; Verifying High-Level Requirements with Runtime Monitoring<br>
</h2>
You must demonstrate that your elevator system meets the high level
requirements that were added in Project 8 (R-T6 through
R-T10).&nbsp;&nbsp; If needed, review the <a
 href="../proj7/runtime_monitoring.html">Runtime Monitoring Overview
from Project 7</a>.<br>
<h3>1.1&nbsp; Write a Description of Your Runtime Monitor<br>
</h3>
To start out, you will provide a written description of your strategy
for runtime monitoring.&nbsp; Take a look at the inputs provided by the
Runtime Monitor class.&nbsp; For each high level requirement state one
or more specific run-time checks you could perform to ensure that
requirement is being met.&nbsp; For example, if a high level
requirement were "never stops at floor 6" you could write a monitor
that threw a warning every time the motor was commanded to stop while
the elevator was at floor 6.&nbsp;&nbsp; You must also include a
statechart
for each requirment that your runtime monitor is checking. The
statechart should
mirror the actual state of the elevator and contain both valid and
invalid states.
An example statechart for the requirement "never stops on floor 6"
might look something like this:<br>
<br>
<div style="text-align: left;"><img style="width: 559px; height: 163px;"
 alt="Sample statechart" src="RTmonitor.png"><br>
<br>
</div>
If your elevator ever enters an invalid state, then you should throw a warning.
In your writeup, address each
of the high level requirements R-T6
through R-T10.&nbsp; (Hint: You may want to use a different statechart for 
each part of R-T8.) Add your
writeup to the <span style="color: rgb(0, 102, 0);">High Level
Requirements Verification</span> page.&nbsp; Your
writeup <strong>must</strong> convince us that your
verification system enforces all high-level
requirements. Your writeup may not exceed 700 words.
<h3>1.2&nbsp; Write a Runtime Monitor<br>
<strong></strong></h3>
<p>Now you will implement the checks you described in part 1.1
above.&nbsp; Add a new class to the simulator.elevatorcontrol package
called Proj11RuntimeMonitor.&nbsp; Be sure you use the right name
because the monitor may be graded by an automated script that relies on
this name.&nbsp; Make sure Proj11RuntimeMonitor extends
simulator.framework.RuntimeMonitor.<br>
</p>
Your runtime monitor must meet the following requirements:<br>
<ul>
  <li>Your runtime monitor <strong>must</strong>
clearly
demonstrate that it
has no false negatives: if there is a violation of any of the
high-level
requirements, the violation <strong>must</strong> be detected and your
verifier <strong>must</strong> clearly indicate which requirement was
violated.&nbsp;</li>
  <li>Your
monitoring code must belong to the <tt>simulator.elevatorcontrol</tt>
package (the same directory as your other code) and be called
Proj11RuntimeMonitor.&nbsp; You must include the
code in the elevatorcontrol/ folder of your portfolio and fully link
and document all Java files in the package.html file, just like all the
rest of your code.</li>
  <li>Your monitor must be a subclass of the <span
 style="font-family: monospace;">simulator.framework.RuntimeMonitor</span>
class.</li>
  <li>Your monitor must check each high level requirement and generate
a warning (using the provided warning() class in the RuntimeMonitor
parent class) if any high level requirement is violated.&nbsp; The
warning <span style="font-weight: bold;">must </span>name the
requirement that was violated and give a descriptive message of how it
was violated.&nbsp; For example , "R-T.6 Violated:&nbsp; Car stopped at
floor 5 FRONT when there were no pending calls."</li>
</ul>
<p><strong></strong><strong>If your monitor does
more than monitor the system (e.g. outputs any framework or network
messages, or affects the simulation state in any way, you will receive
significant deductions on the final assignment.</strong></p>
<p>It is acceptable for your
monitor not to throw warnings during startup, but it must register any
violations of the high level requirements that occur after the elevator
has moved for the first time.<br>
</p>
<p>In order to verify that your design meets the requirements, we will
run your elevator against our own runtime monitor.&nbsp; Although the
grading monitor is not provided, you will receive the output of our
tests in your project feedback.<br>
</p>
<h3>1.3&nbsp; Peer Review the Runtime Monitor</h3>
For this project you will be peer reviewing your Runtime Monitor. You
must submit a formal peer review document <strong>for each portion</strong>
of your
Runtime Monitor that traces to each of the high level requirements R-T6
through R-T10. Make sure to update the Peer Review Log and Issue Log
appropriately.<br>
<h3></h3>
<h3>1.4&nbsp; Execute the Runtime Monitor</h3>
<p>Execute at least the following acceptance tests using the runtime
monitor.&nbsp; Log the tests, the test results, and any comments in the
<span style="color: rgb(0, 102, 0);">High Level Requirements
Verification </span>page of your portfolio.<br>
</p>
<ul>
  <li><a href="../proj10/proj10acceptance1.pass">proj10acceptance1.pass</a></li>
  <li><a href="../proj10/proj10acceptance2.pass">proj10acceptance2.pass</a></li>
  <li><a href="../proj10/proj10acceptance3.pass">proj10acceptance3.pass</a></li>
  <li><a href="proj11acceptance1.pass">proj11acceptance1.pass</a><br>
  </li>
  <li><a href="proj11acceptance2.pass">proj11acceptance2.pass</a><br>
  </li>
  <li><a href="proj11acceptance3.pass">proj11acceptance3.pass</a></li>
</ul>
<h2>2.&nbsp; Complete testing of your design
</h2>
You must run all tests on your latest design:<br>
<ul>
  <li>Pass all unit tests - <span style="font-weight: bold;">all </span>tests
must
pass
with
0
failed
assertions.&nbsp;
You
do
NOT have to run with the "-b 200" option.<br>
  </li>
  <li>Pass all integration tests - <span style="font-weight: bold;">all






    </span>tests must pass with 0 failed assertions. You do NOT have to
run with the "-b 200" option.</li>
  <li>All acceptance tests - rerun all the acceptance tests listed
below.&nbsp;
Be sure to use the "-b 200" option to enable the finite
bandwidth setting.&nbsp; You are <span style="font-weight: bold;">not </span>required
to
pass
these tests
for this project,
but you must include a detailed description of the issues that are
preventing the elevator from passing any test. <br>
  </li>
  <ul>
    <li><a href="../proj7/proj7acceptance1.pass">proj7acceptance1.pass</a></li>
    <li><a href="../proj7/proj7acceptance2.pass">proj7acceptance2.pass</a></li>
    <li><a href="../proj7/proj7acceptance3.pass">proj7acceptance3.pass</a></li>
    <li>proj8group#acceptance1.pass</li>
    <li><a href="../proj10/proj10acceptance1.pass">proj10acceptance1.pass</a></li>
    <li><a href="../proj10/proj10acceptance2.pass">proj10acceptance2.pass</a></li>
    <li><a href="../proj10/proj10acceptance3.pass">proj10acceptance3.pass</a></li>
    <li><a href="proj11acceptance1.pass">proj11acceptance1.pass</a><br>
    </li>
    <li><a href="proj11acceptance2.pass">proj11acceptance2.pass</a><br>
    </li>
    <li><a href="proj11acceptance3.pass">proj11acceptance3.pass</a></li>
  </ul>
</ul>
For your final project handin, you will have to pass all
acceptance tests and turn in a complete and consistent design package,
so plan ahead accordingly.<br>
<br>
<hr>
<h2 style=""> Team Design Portfolio </h2>
<p> The portfolio you submit should contain the most up-to-date design
package for your elevator system organized and formatted according to
the <a
 href="http://www.ece.cmu.edu/%7Eece649/project/portfolio/portfolio_layout.html">portfolio
guidelines</a>.<span style="">&nbsp;</span> You are going to update
your
portfolio every week, so be sure to keep an up to date working
copy.&nbsp;<br>
</p>
<h4> Ensure your design portfolio is complete and consistent. </h4>
The following is a partial list of the characteristics your portfolio
should exhibit:
<ul>
  <li>Changes requested by the TAs in previous projects have been
applied. </li>
  <li>All required documents are complete and up-to-date to the extent
required by the projects (you do not need to update files or links
related to future projects).<br>
  </li>
  <li>All documents include group # and member names at the top of the
document.&nbsp; (This includes code, where this information should
appear in the header field) </li>
  <li>Individual documents have a uniform appearance (i.e., don't look
like they were written by 4 individual people and then pieced together)
  </li>
  <li>The issue log is up to date and detailed enough to track changes
to the project. </li>
</ul>
<hr>
<h2> Handing In Results</h2>
<ul>
  <li> Each team shall submit exactly one copy of the
assignment.<br>
  </li>
  <li>Follow the handin instructions detailed in the
Project FAQ to submit your portfolio into the afs handin directory (<tt>/afs/ece/class/ece649/Public/handin/proj10/group#/ontime/).</tt><span
 style="font-weight: bold;"><br>
    </span></li>
  <li><span style="font-weight: bold;">Be sure you
follow the required format for the directory structure of the portfolio
and its location in the handin directories.</span><br>
  </li>
  <li>Be sure to follow <span style="font-weight: bold;">ALL</span>
the
portfolio guidelines detailed in the <a
 href="../portfolio/portfolio_layout.html">Portfolio Layout</a> page.<b><br>
    </b></li>
  <li><b>Any submission that contains files with modification dates
after the project deadline will be considered late and subject to a
grade deduction</b> (see <a
 href="http://www.ece.cmu.edu/%7Eece649/admin.html#grading">course
policy page</a> for more information). </li>
</ul>
<hr>
<h2> Grading Criteria:</h2>
This project counts as one team grade. Points are assigned as
follows.&nbsp; <a href="proj11_grade_sheet.pdf">A detailed grading
rubric is available here.</a>
<p><b> This project assignment is worth 115 points: </b>
</p>
<ul>
  <li><span style="font-weight: bold;">20 points</span> for 4 peer
reviews of the updated Runtime Monitor in the <span
 style="font-weight: bold;">Peer
Review
Log</span>.<br>
  </li>
  <li><span style="font-weight: bold;">20 points </span>for completely
passing all unit tests<br>
  </li>
  <li><span style="font-weight: bold;">20 points </span>for completely
passing all integration tests </li>
  <li><span style="font-weight: bold;">20 points </span>for running
all acceptance tests and either passing the
tests or logging the results.</li>
  <li><span style="font-weight: bold;">30
points </span>for your
runtime monitor, description, and High Level Requirements Verification
log.</li>
  <li><span style="font-weight: bold;">5 points</span> for an entry in
the <span style="font-weight: bold;">Improvements Log </span>that
tells us what
can be improved about this project. If you
encountered any minor bugs that we haven't already addressed, please
mention them so we can fix them. If you have no suggestions, say so in
your entry for this project.</li>
</ul>
<hr style="width: 100%; height: 2px;">Back to <a
 href="../../index.html">course home page</a>
<p> </p>
</body>
</html>
