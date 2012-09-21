649-Team-2
==========

Elevator project for 18-649.

<html>
<h2> Assignment </h2>
<p> You will transform your requirements specification from an
event-triggered
system to a time-triggered one, and complete a detailed design
document. No
longer are there discrete events that cause information processing and
state
changes in your controllers. In your time-triggered system, once during
each
cycle (we use cycle here as some unit of time determined by processing
and
control needs) a time triggered machine looks at the information it has
available to it, possibly calculates some internal variables, and
decides
whether it should transition to a new state. For each non-environmental
object,
you will complete a detailed design using UML state charts as described
in the
required reading. </p>
<p> <strong>PLEASE KNOW THAT CODE AND DESIGN ARE DIFFERENT THINGS.</strong>
Code should be well designed, but there should be no actual code in
your
design. If there is code, it is not a design, and so it will be graded
as code
and not a design---which means you will earn no points for that
submission
item. Pseudo code is acceptable for expressing guard conditions, state
outputs,
and so on, but you should use it as little as possible. This applies to
requirements as well as statechart actions and trigger conditions.<br>
</p>
<p> The procedure for this assignment is: </p>
<ol>
  <li>Rewrite your event-triggered behavioral requirements to be
time-triggered.
This means that instead of acting on events (like receiving a message),
the
controller modifies its outputs based on the current value of state
variables.
When you found yourselves doing tricky state variable manipulation to
get a
correct event-triggered behavior, you will find that time-triggered
requirements make your design much simpler.&nbsp;</li>
</ol>
<ul>
  <li>You can assume that each controller maintains a copy of the
system state
that consists of the most recent message values for all messages the
controller
receives.&nbsp; For simplicity, you should continue to use the
mMessage[]
notation (e.g. mHallCall[f,b,d]) to refer to these state variables.<br>
  </li>
  <li>Make sure that you have a complete set of time-triggered
requirements.&nbsp; It is up to you whether you leave your
event-triggered
requirements in place or remove them and replace them with
time-triggered
requirements.&nbsp; If you choose not to remove the event triggered
requirements, make sure that you us a different numbering scheme for
the two
sets so that there is no ambiguity..<br>
  </li>
  <li>You will need to update your requirements-to-sequence-diagrams
and
requirements-to-constraints traceability to reflect the new
requirements you
have written.&nbsp; Since only the time-triggered requirements will be
used as
you go forward, it is acceptable to trace only the time-triggered
requirements
(i.e. in the traceability, you can let the TT requirements replace the
ET
ones).&nbsp;</li>
  <li><span style="font-weight: bold;">NOTE:&nbsp; The format for
time-triggered
requirements is slightly different from those presented in the lecture,
and
this assignment supercedes what was presented in the lectures.&nbsp; </span>Read
the
discussion
below on <a href="#timetriggered">Time-Triggered
Design</a> for more guidelines.&nbsp; <br>
    <br>
  </li>
</ul>
<ul>
  <li>Keep track of changes to your design using the issue log.&nbsp;
If you make
any substantive changes to your behavioral requirements (such that an
outside
observer would notice a different behavior), you must document those
changes in
the issue log which is included with the <a
 href="../portfolio/portfolio_layout.html">project portfolio
template</a>.&nbsp; If, in the course of writing requirements or
statecharts,
you find that you must add or modify your scenarios and sequence
diagrams, you
must document these changes in the issue log as well.</li>
</ul>
<ul>
  <li>In the course of rewriting your event-triggered requirements as
time-triggered, you only need to log changes if you change the behavior
of the
system.&nbsp; If the new requirements have the same function as the old
requirements, no log entry is necessary.<br>
    <br>
  </li>
</ul>
<ul>
  <li>Design a state chart for each non-environmental control object:
    <ul>
      <li>CarButtonControl </li>
      <li>CarPositionControl </li>
      <li>Dispatcher </li>
      <li>DoorControl </li>
      <li>DriveControl </li>
      <li>HallButtonControl </li>
      <li>LanternControl </li>
    </ul>
The state charts shall be <b>time-triggered</b> with guard conditions,
    <b>not</b> event-triggered. Although you may not agree with this
design choice,
you are required to design a pure time-triggered system.&nbsp; Time
triggered
design is a often a good choice for reliable, distributed systems, and
our goal
is to teach you this technique.&nbsp; For detailed information about
time-triggered design, see the following section of this writeup on <a
 href="index.html#timetriggered">Time-Triggered Design</a> and
the course
lecture notes.<br>
    <br>
  <b><u>NOTE: The statecharts should fully describe your working elevator. You should have a complete Sabbath elevator design by the end of this week. This may require you to create more use cases or scenarios than what we originally gave you. </u></b><br>
  <br> 
  </li>
  <li>Ensure backward and forward traceability by documenting the
relationships
between each behavior requirement and each state chart transition arc
or state.
Only trace the time-triggered requirements.<br>
    <ul>
      <li>Forward traceability means directly relating each requirement
in the
specification to one or more states and/or transitions. </li>
      <li>Backward traceability means directly relating each state or
transition to
one or more requirements. </li>
    </ul>
In order to do forward and backward traceability in one table,
construct a
table with the requirement numbers across the first row and the states
and
transitions down the first column.&nbsp; Put an X in the appropriate
cells to
indicate that a state/transition supports a requirement. See <a
 href="../proj1/testlight.html">the example</a>. A correct design
will have
at least one state or transition for each requirement and at least one
requirement for each state or transition. So, every row and every
column should
have at least one X in it. <br>    
    <br>
  <b><u>NOTE: For this project, nothing should trace to future expansion except network messages that are transmitted but never received.</u></b><br>
  <br>
  </li>
  <li>Perform a peer review of your statecharts. <span
 class="Apple-style-span"
 style="border-collapse: separate; color: rgb(0, 0, 0); font-family: 'Times New Roman'; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; font-size: medium;">You

must
complete
a
peer review checklist for each statechart ie. one peer
review
per object. Record the results in a<span class="Apple-converted-space">&nbsp;


    </span><a href="http://www.ece.cmu.edu/%7Eece649/project/misc/peer_review.xls">peer
review
sheet</a><span class="Apple-converted-space">&nbsp;</span>and also
complete a log entry into peer review log. Recording defects on the
peer review
sheet does NOT result in point deductions. Be honest.</span></li>
</ul>
<ul>
  <li>Turn in your complete team design package. </li>
</ul>

</html>
