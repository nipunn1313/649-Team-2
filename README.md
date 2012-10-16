649-Team-2
==========

Elevator project for 18-649.

<html>
  <body>
    <p><span style="color: red">
Update:
<ul>
<li style="color: red"> Fixed statement about leveling: You MUST be able to pass the acceptance test with leveling <b>enabled</b>.</li> 
<li style="color: red">There is a simulator flag to disable it that you may use for debugging purposes only.</li>
</ul></span>
</p>
<hr style="width: 100%; height: 2px;">
<h2>Assignment:</h2>
Throughout this semester your group has designed, implemented, and
tested a basic elevator system.&nbsp; Before you proceed
with optimization, you will make sure the entire design package is 1)
organized well and 2) consistent with your current
implementation.&nbsp;&nbsp; Your design will also need to pass all
previous tests and the additional tests provided for this
project.&nbsp; If you have been following the process in the prior
design stages, this
project should be pretty straightforward. <br>
<br>
<b>Note that this project is more heavily weighted than previous projects.</b><br>
<hr>
<h3>What you need to do:</h3>
1) Make sure you have the latest version of the simulation framework
from the <a href="../codebase/download.html">download page</a>.<br>
<br>
<span style="font-weight: bold; text-decoration: underline;">A note
about control periods:</span><br>
The control periods for controllers instantiated during acceptance
tests are defined in MessageDictionary.java.&nbsp; For this project,
the
following control periods are <span style="font-weight: bold;">required</span>.&nbsp;
Note
that
these

are
the
default

periods
in
the simulator, so if
you have not changed them, you should be fine.<br>
<ul>
  <li>HallButtonControl:&nbsp; 100ms</li>
  <li>CarButtonControl:&nbsp; 100ms</li>
  <li>LanternControl:&nbsp; 200ms</li>
  <li>CarPositionControl: 50ms</li>
  <li>Dispatcher: 50ms</li>
  <li>DoorControl: 10ms</li>
  <li>DriveControl: 10ms</li>
</ul>
We believe these control periods are reasonable.&nbsp; If you wish to
change
the control period used by any controller, you MUST obtain the approval
of the course staff.&nbsp; In order to convince us, you will need
to
make an argument based the timing of the physical system.&nbsp;
"Because it
makes my elevator work" is not a valid argument!<br>
<br>
If you obtain TA approval, you <span
 style="font-weight: bold; font-style: italic;">must</span> write up
the justification for the change and include it in the description of
the relevant control object(s) in the <span
 style="color: rgb(51, 51, 255);">Elevator Control package </span>(<span
 style="color: rgb(51, 51, 255);">elevatorcontrol/package.html</span>)
in your portfolio.&nbsp; You should also record the name of the TA
that approved the change.&nbsp; If you either a) do not obtain TA
approval or
b) do not note the changes and the justification for the changes in
your portfolio, significant
points will be deducted.<br>
<span style="font-weight: bold;"><br>
2) Organize your design portfolio</span>.
<br>
&nbsp;&nbsp;&nbsp; Make sure that your portfolio documents
conform to the structure and guidelines described in the projects and
the <a href="../portfolio/portfolio_layout.html">portfolio page</a>.&nbsp;
If
you
have
been
using

the
portfolio
template

and
following
the project
guidelines, you should have very little work to do here.<br>
<br>
<span style="font-weight: bold;">3) Complete your integration tests</span>,
including
all
the

steps
specified
in

project
5
(logs, reviews,
traceability, summary files, etc).<br>
<ul>
  <li>If you have more than 20 sequence diagrams, you need only create
tests for 20 of them.&nbsp; However you MUST test the original
scenarios provided in the portfolio template (1A, 1B, 1C, 2A, 2B, 3A,
4A, 5A, 5B, 6, 7A, 7B, 7C, 8A, 9A)<br>
  </li>
  <li>If you have fewer than 20 sequence diagrams, you must test them
all.</li>
  <li>You must pass all integration tests.</li>
</ul>
<span style="font-weight: bold;">4) Peer review your integration tests.
</span>You should peer review at least 4 of the newly created
integration tests for this project. Although you are only required to
review 4, we highly encourage you to do more to ensure testing coverage.<br>
<br>
<span style="font-weight: bold;">5) Ensure your design portfolio is
complete and consistent.&nbsp;</span> If you have been keeping up with
updates in the previous project stages, most of this work will already
be done. The following is a partial list of the characteristics your
portfolio should exhibit: <br>
<ul>
  <li>Problems identified in previous assignments have been corrected.<br>
  </li>
  <li>All documents are complete and up-to-date with latest
time-triggered implementation (i.e., code matches statecharts,
requirements, sequence diagrams, and all traceability is complete and
up to date)</li>
  <li>All documents include group # and member names at the top of the
document.&nbsp; (This includes code, where this information should
appear in the header field)</li>
  <li>Individual documents have a uniform appearance (i.e., don't look
like they were written by 4 individual people and then pieced together)</li>
  <li>Code is commented sufficiently and reflects code-statechart
traceability.<br>
  </li>
  <li>Issue log is up-to-date and detailed enough to
track changes to the project.<br>
  </li>
  <li>etc.<br>
  </li>
</ul>
<span style="font-weight: bold;">6) Use Runtime Monitoring to Study
Performance</span><br>
<br>
For this part of the project, you will use the runtime monitoring
framework to study the performance of your elevator.&nbsp; Later on, in
Project 11, you will use the runtime monitoring to verify that your
final design has met the high level requirements.<br>
<br>
First, read the description of the runtime monitoring framework in the <a
 href="runtime_monitoring.html">Runtime Monitoring Overview</a>.&nbsp; <br>
<br>
Then use the SamplePerformanceMonitor.java as a starting place to
create your own runtime monitor.&nbsp; Add a new class to the
simulator.elevatorcontrol package
called Proj7RuntimeMonitor.&nbsp; Be sure you use the right name
because the monitor may be graded by an automated script that relies on
this name.&nbsp; Make sure Proj7RuntimeMonitor extends
simulator.framework.RuntimeMonitor.<br>
<br>
Your monitor shall record the following
performance information:<br>
<ul>
  <li><span style="font-weight: bold;">How many times the elevator
became overweight </span>- Specifically, count the number of stops
where the car became overweight
at least once.&nbsp; If the doors close completely and reopen at the
same
floor, that counts as two overweight instances.&nbsp; Code to meet this
requirement is provided in SamplePerformanceMonitor. </li>
  <li><span style="font-weight: bold;">How many wasted openings </span>-
Count

the
number
of

times
when
a door opens but there is no call at
that floor.&nbsp; Depending on your design, you may need to use button
presses, light indicators, or changes in car weight to determine
whether or not an opening was wasted.</li>
  <li><span style="font-weight: bold;">How much time was spend dealing
with door reversals</span> - count
the time from a reversal until the doors fully close, and accumulate
this value over the whole test.&nbsp; Hint:&nbsp; check out the
StopWatch class
in SamplePerformanceMonitor. Note: If you are only using NUDGE for doors, you
will not see any time spent dealing with door reversals. This is ok, but you must
implement this anyway as it will be useful in later projects.<br>
  </li>
</ul>
Make sure you implement the summarize() method to report these results
at the end of the test.<br>
<b>You must also complete a peer review of your runtime monitor and add it to the
peer review log</b>
<br>
You are not required to improve your design based on these metrics,
only to measure the performance of your current design.&nbsp; However,
it
should get you thinking about ways to improve your design and the
performance of your elevator.<br>
<p><strong></strong><strong>If your monitor does
more than monitor the system (e.g. outputs any framework or network
messages, or affects the simulation state in any way, you will receive
significant deductions on the assignment.</strong></p>
<span style="font-weight: bold;"><br>
7) Pass an acceptance test.</span> <br>
<br>
Read the <a href="../proj5/testing-requirements.html#acceptancetest">Acceptance
Testing
section
of
the
Testing
Requirements</a>.&nbsp;&nbsp; Running an
acceptance test involves:<br>
<ul>
  <li>Run the acceptance test and observe the results of the test. <br>
  </li>
  <li>Be sure to run your Proj7RuntimeMonitor during the test.<br>
  </li>
  <li>Save the test input and output files in the acceptance_test/
folder of your portfolio</li>
  <li>Record the results of the test, including the monitoring results,
in the <span style="color: rgb(0, 153, 0);">Acceptance Test Log</span>.</li>
  <li>Add entries to the <span style="color: rgb(0, 153, 0);">Issue Log</span>
documenting any defects identified as a result of the test.&nbsp; Be
sure to include the issue log #'s in the <span
 style="color: rgb(0, 153, 0);">Acceptance Test Log</span> file as well.</li>
</ul>
We have provided three acceptance tests:<br>
<ul>
  <li><a href="proj7acceptance1.pass">proj7acceptance1.pass</a></li>
  <li><a href="proj7acceptance2.pass">proj7acceptance2.pass</a> <br>
  </li>
  <li><a href="proj7acceptance3.pass">proj7acceptance3.pass</a></li>
</ul>
In order
to receive full credit for this assignment, you must run all three
tests with a random seed of 8675309 and document the test
results.&nbsp; You must pass <span style="font-weight: bold;">proj7acceptance1.pass</span>.&nbsp;
This

constitutes
"have
a

working

elevator"
for
the purposes of the midsemester project handin.&nbsp; You
are not required to pass <span style="font-weight: bold;">proj7acceptance2.pass
</span>and <span style="font-weight: bold;">proj7acceptance3.pass</span>,
but

you
can
do

so
for
bonus points (see below).&nbsp; For any of the
three tests, if your elevator does not pass the test, you must document
the problem in the Acceptance Test Log and the Issue Log.<br>
<br>
<span style="font-weight: bold;">Note 1: </span>You may notice
passengers not boarding/leaving your elevator. This is possibly because
you haven't correctly implemented the leveling speeds for the drive controller.
To handle situations such as cable slip when the elevator is overweight, you will
need to level with the doors open.
See the requirements for the Passenger and the DriveControl speeds for more information. <br>
<br>
<span style="font-weight: bold;">Note 2:&nbsp; </span>If you do not
pass
<span style="font-weight: bold;">proj7acceptance1.pass </span>by the
time you hand in Project 7, you MUST (eventually) pass
this test in order get a grade for this course.&nbsp; In that case,
contact the course staff to arrange a demonstration when you are ready.<br>
<br>
<span style="font-weight: bold;">Note 3: </span>You are strongly
discouraged in this project to use exception handling. Starting in
Project 8, you will be forbidden to do so without course staff approval.<br>
<span style="font-weight: bold;"></span><br>
<span style="font-weight: bold; color: rgb(153, 51, 153);">8)
BONUS:&nbsp; pass additional
acceptance tests</span><br>
<br>
You can earn bonus points by also passing&nbsp;<a
 href="proj7acceptance3.pass"></a><span style="font-weight: bold;">proj7acceptance2.pass
</span>and <span style="font-weight: bold;">proj7acceptance3.pass</span>.&nbsp;
These
tests

model
the
up-peak

and
down-peak
conditions with a
light-to-moderate passenger load.&nbsp; You must fully document the
test results and any associated bugs (as described in Part 4 above)
even if your elevator does not pass these tests.&nbsp; Note that you
will eventually be required to pass these tests in Project 8.<br>
<br>
The bonus is substantial (1% of your total course grade), but the
requirements for getting the bonus are also substantial!&nbsp;&nbsp; In
order to be eligible for bonus points, you must:<br>
<ul>
  <li>Successfully pass (and document) all unit, integration, and
acceptance tests required by the project.</li>
  <li>Have a complete and consistent design portfolio (see the grading
rubric for more details)</li>
  <li>Turn the project in on time.<br>
  </li>
</ul>
If you do not meet all these criteria, you will not get bonus points
even if your elevator passes the bonus acceptance tests.&nbsp;&nbsp;
This is
because we don't want you to ignore other parts of the project in order
to try and hack together an elevator that passes the additional
acceptance tests.&nbsp; We want to give you an incentive to submit a
complete design package.&nbsp; We also want to reward teams that have
put in a substantial effort on the project.<span
 style="font-weight: bold;"></span><br>
<ul>
</ul>
<hr>
<h2> Handing In Results</h2>
<p class="MsoNormal"> Each team shall submit exactly one copy of the
assignment.<br>
</p>
<p class="MsoNormal">Follow the handin instructions detailed in the
Project FAQ to submit your portfolio into the afs handin directory (<tt>/afs/ece/class/ece649/Public/handin/project7/group#/ontime/).<br>
</tt></p>
<p class="MsoNormal">Be sure you follow the required format for the
directory structure of the portfolio and its location in the handin
directories.<tt><br>
</tt></p>
<p>Be sure to follow <span style="font-weight: bold;">ALL</span> the
portfolio guidelines detailed in the <a
 href="../portfolio/portfolio_layout.html">Portfolio Layout</a> page.<br>
</p>
<p><b>Any submission that contains files with modification dates
after the project deadline will be considered late and subject to a
grade deduction</b> (see <a
 href="http://www.ece.cmu.edu/%7Eece649/admin.html#grading">course
policy page</a> for more information). </p>
<hr style="width: 100%; height: 2px;">
<h2>Grading (<span style="font-weight: bold;">135 Points + 10 bonus
points)</span>:</h2>
<p>This assignment counts as one team grade. If you choose to divide
the work, remember that you will be graded on the whole assignment.<br>
</p>
<p><span style="font-weight: bold;">&nbsp;</span><a
 href="proj7_grade_sheet.pdf">A detailed
grading rubric is given here.</a>&nbsp; Grading will be as follows:<br>
</p>
<ul>
  <li><span style="font-weight: bold;">15 points </span>- for
Proj7RuntimeMonitor that records the stated performance requirements.<br>
    <span style="font-weight: bold;"></span></li>
  <li><span style="font-weight: bold;">15 points </span>- for a design
portfolio that meets the portfolio layout requirements</li>
  <li><span style="font-weight: bold;">20 points - </span>peer reviews
for at least 4 of the integration tests and the runtime monitor<br>
  </li>
  <li><span style="font-weight: bold;">35 points </span>- for complete
unit and integration testing and running three acceptance tests, and
passing proj7acceptance1.pass</li>
  <li><span style="font-weight: bold;">45 points </span>- for a
complete and consistent portfolio.&nbsp; Note that the grading criteria
are based on sampling.&nbsp; If we check one part of your portfolio and
find problems, chances are that there are problems in other parts as
well.<br>
  </li>
  <li><span style="font-weight: bold;">5 points </span>-
for an improvements log entry.&nbsp; If you
encountered any minor bugs that we haven't already addressed, please
mention them so we can fix them. If you have no suggestions, say so in
your entry for this project.</li>
  <li><span style="font-weight: bold; color: rgb(153, 51, 153);">10
points</span><span style="color: rgb(153, 51, 153);"> - for passing 2
bonus acceptance tests (points only awarded if the rest of project has
been substantially completed)</span><br>
    <br>
  </li>
</ul>
  </body>
</html>
