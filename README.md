649-Team-2
==========

Elevator project for 18-649.

<html>
<h2>2.&nbsp; Controller Implementation</h2>
Implement (in Java) the DoorControl, DriveControl, HallButtonControl,
and CarButtonControl objects. These correspond to some of objects you
have previously designed in state charts.<br>
<span style="font-weight: bold;"><br>
</span>Each controller object you write interfaces with the simulated
physical system and the simulated network.&nbsp; Here are some
requirements for the controllers:<br>
<ol>
  <li>Each controller shall extend the <span style="font-weight: bold;">simulator.framework.Controller</span>
class. </li>
  <li>Each controller shall be placed in the <span
 style="font-weight: bold;">simulator.elevatorcontrol</span> package.<br>
  </li>
  <li>Each controller shall use the network and physical interface
objects provided in the Controller super class and no other network
connection objects. </li>
  <li>Each controller shall receive, at most, one physical input and
generate, at most, one physical output.&nbsp; This means that each
physical message payload shall be sent by, at most, one object and
received by, at most, one object.&nbsp; For example, if your
DriveController listens to the DriveSpeed framework message, your
Dispatcher may NOT listen to DriveSpeed framework message as
well.&nbsp; If your DriveController sends Drive framework messages,
your Dispatcher may NOT send Drive Framework messages as well. </li>
  <li>Each controller shall receive and send network and framework
messages according to the interfaces defined in the Elevator Behavioral
Requirements document. </li>
  <li>You may not create additional communication channels of any kind
between the controllers.&nbsp; This includes creating shared references
to static/global variables and any method where a controller may
directly modify the state of another controller.&nbsp; If you are in
doubt as to whether something you are doing violates this requirement,
consult a TA in office hours.</li>
  <li>Each controller shall execute <span style="font-weight: bold;">at
most</span> one transition per execution of the control loop, and the
control loop must execute periodically.&nbsp; To schedule controller
code in real systems, there must be
a worst-case bound on the execution time.&nbsp; If the controller is
allowed to make multiple transitions, then there is (theoretically) no
upper bound on how long it takes to execute the controller.&nbsp; The
one-transition-per-loop
rule also approximates the limited processing power of the small
microcontrollers you would likely see in a highly distributed
application.</li>
</ol>
<p> In addition to the above:<br>
</p>
<ul>
  <li>All Java files you write must be placed in the <span
 style="font-weight: bold;">simulator.elevatorcontrol</span>
package.&nbsp;<br>
  </li>
  <li>You may implement additional utility classes in
the <span style="font-weight: bold;">simulator.elevatorcontrol</span>
package, provided that these do not create
an additional communication channel between the controllers.&nbsp; The
Utility class provided (in Utility.java) has some examples of how to
build arrays to receive and process all the copies of replicated
network messages.<br>
  </li>
  <li>Your portfolio has a place to include your elevator code.&nbsp;
You must submit ONLY the contents of the <span
 style="font-weight: bold;">simulator.elevatorcontrol</span>
package, and the files must be
placed in the <span style="font-weight: bold;">elevatorcontrol/</span>
folder of your portfolio.&nbsp; See this note in the<a
 style="color: rgb(204, 0, 0);" href="../portfolio/index.html#code">
portfolio layout page</a> for
details.<br>
  </li>
  <li>Your code must be compatible with the latest required code
release.&nbsp; (Note the details of this in the <a
 href="../project-faq.html#bug">bug handling policy</a>).<br>
  </li>
</ul>
A couple of notes regarding the CAN network implementation:<br>
<ul>
  <li>Although you are going to be able to define your own bit-level
message payloads, you are still required to adhere to the message
dictionary and interface requirements we have provided.&nbsp; This
means that you <span style="font-weight: bold;">may not</span> do any
of the following: </li>
  <li
 style="list-style-type: none; list-style-image: none; list-style-position: outside; display: inline;">
    <ul>
      <li>Add more information (e.g. another variable value) to a
message that has already been defined </li>
      <li>Define a new message. </li>
      <li>Use a message for a purpose other than its stated purpose. </li>
    </ul>
  </li>
  <li>If you violate these constraints, it will result in a significant
point deduction.<br>
  </li>
  <li>Regarding CAN payload translators and message IDs, the acceptance
tests for this project will be run on a simulated CAN bus with
'infinite' bandwidth, so performance and message prioritization will
not be an issue.&nbsp; There will be a project later in the semester
where you
will deal with optimizing the network performance.&nbsp;&nbsp; For this
project and the next project,
it is
acceptable to use the CAN Translators and CAN message ID's that we have
provided, even though they do not make efficient use of network
bandwidth.&nbsp; If you need to write additional translators for
messages sent by your controllers, you may do so.</li>
  <li>All this is spelled out in
more detail in the <a href="../codebase/development-overview.html">simulator
development
overview</a>.&nbsp; <br>
  </li>
</ul>
As you continue to develop your project, you should also continue to
log defects and changes in the logs that you started during project 4.<br>
<br>
<h2>3.&nbsp; Traceability - Statecharts to Code</h2>
You are REQUIRED to mark the line of code that causes each and every
state transition (i.e. forward traceability) you have designed on your
state charts. This marking will be accomplished with comments. So that
these lines are easily distinguished from other comments, you shall
begin the line with '//' (to start the comment), followed by the
character '#', followed by the word 'transition', followed by a space,
followed by the transition name as described on your state machine
backward traceability matrix in single quotes.
<p> For example, on the line of code that corresponds to the DC.1
transition on your Drive Controller statechart traceability matrix, you
would add the comment: </p>
<pre>//#transition 'DC.1'<br>

</pre>
just above the line in your code that actually CAUSES this
transition.&nbsp;
The appropriate line may depend on your implementation, but in general,
it
would be appropriate to place the traceability comment just above the
if
statement that tests the guard conditions for the transition.<br>
<p> The purpose of the traceability is so that it is possible to easily
check to ensure all of the arcs in your state transition diagram are
implemented as you have described them.<br>
</p>
<p> In order to ensure that you have traced all your arcs, you will add
an entry to the Statecharts to Code Traceability file (<span
 style="color: rgb(0, 153, 0);">traceability/sc_code.html</span> in the
portfolio template)<br>
</p>
<p> Controller Name (e.g. DoorControl)<br>
Module Author:&nbsp; Module author's name<br>
Traceability performed by:&nbsp; Team member's name<br>
Line Number&nbsp;&nbsp;&nbsp; Transition #<br>
Line Number&nbsp;&nbsp;&nbsp; Transition #<br>
.... </p>
<p> You may use a table or other basic formatting to organize this
information. The line number refers to the line that the <span
 style="font-weight: bold;">comment</span> appears on.&nbsp; Line
numbering shall include empty lines.<span style="font-weight: bold;"><br>
Hint:&nbsp;</span> the following linux commands may help you generate
some of the required output:&nbsp;&nbsp;<br>
<span style="font-family: monospace;">&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp; nl -b a Dispatcher.Java | grep "#transition"<br>
</span>where Dispatcher.Java is the name of the controller Java file.<span
 style="font-family: monospace;"><br>
</span> </p>
<p> Someone other than the person who authored the module must generate
this check and verify that every transition traces to the code.<br>
<br>
</p>
<h2><span style="font-weight: bold;">4.&nbsp; Unit Testing</span></h2>
<p> You will write and execute a unit test for each controller you have
implemented (DoorControl, DriveControl, HallButtonControl, and
CarButtonControl objects).&nbsp; The list below summarizes the steps
you must follow.&nbsp; These steps are explained in more detail in the <a
 href="testing-requirements.html#unittest">Unit Test section of the
Testing Requirements</a>.&nbsp; <span style="font-weight: bold;"><br>
</span> </p>
<ul>
  <li>Create unit tests that tests (a configuration / .cf file and one
or more message injector / .mf files) that exercise all transitions of
the statechart.&nbsp;<br>
  </li>
  <ul>
    <li>If you have transitions with guard conditions combined by OR
operator, you must separately test each OR term. </li>
    <li>Each state you test must use assertions to test <span
 style="font-weight: bold;">all </span>controller outputs (both
network and framework messages).&nbsp; You may optionally use state
assertions to verify the state of the controller as well.<br>
    </li>
  </ul>
  <li>Traceability - injections to exercise transitions and assertions
to test state outputs must be preceded by comments of the format
described in the Testing Requirements document.<br>
  </li>
  <li>Execute the test and record the test results in the <span
 style="color: rgb(0, 153, 0);">Unit Test Log</span> in your
portfolio.&nbsp;<br>
  </li>
  <ul>
    <li>For this project, you are not required to pass all tests.</li>
    <li>All the tests you turn in (for this and all future projects)
must by syntactically valid.&nbsp; That is, they must not cause a
runtime exception.<br>
    </li>
    <li><span style="font-weight: bold;">You will eventually be
required to pass these tests.&nbsp; Don't blow them off now, you'll
just make more work for yourself later.</span> </li>
  </ul>
  <li
 style="list-style-type: none; list-style-image: none; list-style-position: outside; display: inline;">
    <br>
  </li>
  <li>Add your unit tests to the <span style="color: rgb(0, 153, 0);">Unit
Test
Summary
File</span><br>
  </li>
  <li>If the unit test identifies bugs in your design, add the bug to
the <span style="color: rgb(0, 153, 0);">Issue Log</span>, and add a
note in the <span style="color: rgb(0, 153, 0);">Unit Test Log</span>
stating "This test identified issue #X in the Issue Log." </li>
</ul>
<span style="font-style: italic; text-decoration: underline;"><span
 style="font-weight: bold;">Note:</span></span> <span
 style="text-decoration: underline;">This list is just a summary.&nbsp;
You are required to follow all the steps and procedures in the Testing
Requirements document!</span><br>
<br>
<h2><span style="font-weight: bold;">5.&nbsp; Integration Testing</span></h2>
Choose TWO sequence diagrams to write integration tests.&nbsp; The
sequence diagrams you choose for this test must contain at least one of
the four control objects created in this project (DoorControl,
DriveControl, HallButtonControl, and CarButtonControl) and must not
contain any of the other three control objects (Dispatcher,
LanternControl, CarPositionControl).&nbsp; <br>
<br>
A summary of the steps you must follow is given below.&nbsp; Read the <a
 href="testing-requirements.html#integrationtest">Integration Test
section of the Testing Requirements</a> for more details.<br>
<ul>
  <li>Create a sequence diagram test (one configuration /.cf file and
one message injector/.mf&nbsp; file) to test each sequence diagram.</li>
  <li>Traceability - each message injection or assertion must be
preceded by a comment of the format described in the Testing
Requirements.</li>
  <li>Execute the test and record the test results in the <span
 style="color: rgb(0, 153, 0);">Integration Test Log</span> in your
portfolio.&nbsp;<br>
  </li>
  <ul>
    <li>You are <span style="font-weight: bold;">not</span> required
to pass the tests for this project. </li>
    <li>All the tests you turn in (for this and all future projects)
must by syntactically valid.&nbsp; That is, they must not cause a
runtime exception. </li>
    <li><span style="font-weight: bold;">You will eventually be
required to pass these tests.&nbsp; Don't blow them off now, you'll
just make more work for yourself later.</span></li>
  </ul>
  <li>Add your integration tests to the <span
 style="color: rgb(0, 153, 0);">Integration Test Summary File</span><br>
  </li>
  <li>If the unit test identifies bugs in your design, add the bug to
the <span style="color: rgb(0, 153, 0);">Issue Log</span>, and add a
note in the <span style="color: rgb(0, 153, 0);">Integration Test Log</span>
stating "This test identified issue #X in the Issue Log." </li>
</ul>
<span style="font-style: italic; text-decoration: underline;"><span
 style="font-weight: bold;">Note:</span></span> <span
 style="text-decoration: underline;">This list is just a summary.&nbsp;
You are required to follow all the steps and procedures in the Testing
Requirements document!</span><br>
<h2><span style="font-weight: bold;">6. Peer Review</span></h2>
You must perform peer reviews of the 4 controller implementations and 4
unit tests that you have created. Another member of the group that was
not the author of the implementation or unit test should conduct the peer
review.<br>
<br>
For this week's peer review you must complete the following:<br>
<ul>
  <li>As a group, peer review 4 controller implementations.</li>
  <li>As a group, peer review 4 unit tests.</li>
  <li>All peer reviews must be added to the <span
 style="color: rgb(0, 153, 0);">Peer Review Log</span>.</li>
  <li>If a defect is found in a peer review but not fixed this week, it
must be logged in the <span style="color: rgb(0, 153, 0);">Issue Log</span>.<br>
  </li>
</ul>
<br>

</html>
