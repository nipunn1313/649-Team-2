649-Team-2
==========

Elevator project for 18-649.

<html>
<body>
<br>
<center>
<h1> 18-649 Project 8 </h1>
<h2> Smart Dispatcher, Fast Speed, and Correct Lanterns </h2>
<h3> <span style="font-weight: bold;">Check the
<a href="http://www.ece.cmu.edu/%7Eece649/">course webpage</a> for due
dates </span><b><br>
</b></h3>
</center>
<p> Please submit all project-related correspondence to
<img alt="" src="../e-mail_addr.gif"> </p>
<hr>
<p>Changelog: </p>
<ul>
<li>3/16/2011: new R-T5 renumbered to R-T9 to avoid naming collision
with
existing R-T5.</li>
<li>10/12/2011: R-T10 added</li>
<li>11/08/2011: Modified line contatining R-T6 to R-T10 traceablitly<br>
</li>
</ul>
<hr>
<p>For the midterm project, your group specified, designed and
implemented a
simple (but inefficient) elevator.&nbsp; In this project, you will
complete
testing of your simple design.&nbsp; You will also begin changes to the
dispatcher that will improve the performance of your elevator.&nbsp; In
addition to more sophisticated dispatcher design, we will add some new
high
level requirements that require more sophisticated behaviors from other
parts
of the elevator.&nbsp; <br>
</p>
<h2>Assignment</h2>
<h2>1.&nbsp; Complete testing of the simple design</h2>
<p>Your simple design must continue to pass all unit and integration
tests.&nbsp; Since this was a requirement for project 7, this part
should
already be done.<br>
<strong>Note: You are not permitted to use the -il flag </strong><br>
In project 7, you were only required to pass a simple acceptance
test.&nbsp;
You must complete testing of your design to pass the following
acceptance
tests.<br>
</p>
<ul>
<li><a
href="http://www.ece.cmu.edu/%7Eece649/project/proj7/proj7acceptance1.pass">proj7acceptance1.pass</a>
</li>
<li><a
href="http://www.ece.cmu.edu/%7Eece649/project/proj7/proj7acceptance2.pass">proj7acceptance2.pass</a>
</li>
<li><a
href="http://www.ece.cmu.edu/%7Eece649/project/proj7/proj7acceptance3.pass">proj7acceptance3.pass</a>
</li>
<li>One acceptance test of your own design*</li>
</ul>
<p>*Note:&nbsp; The acceptance test your write must meet the following
requirements:<br>
</p>
<ul>
<li>The test must contain at least 20 passengers.&nbsp; <br>
</li>
<li>Every landing must be used at least once as a source or
destination for a
passenger.&nbsp; Note that some floors have more than one landing (e.g.
    1 FRONT
    and 1 BACK), so both the 1 FRONT and 1 BACK landings must be used at
least once
in the test.</li>
<li>You must include a comment in the test file that describes the
scenario you
are testing.<br>
</li>
<li>Your test must be substantially different from the previous tests.</li>
<li>The test file shall be called "proj8group#acceptance1.pass",
  where the # symbol is a placeholder for your group number.<br>
  </li>
  </ul>
  <p>Another change in the testing procedure is related to the random
  seeds used
  to execute the acceptance tests.&nbsp; Because of the pseudorandom
  nature of
  the elevator simulation, if your elevator has subtle bugs, it may pass
  a test
  with one random seed and fail with another.&nbsp; In project 7, the
  random seed
  used by the TAs for grading purposes was provided.&nbsp; In this
  project 8, an
  arbitrary random seed will be used, and it will not be provided ahead
  of
  time.&nbsp;&nbsp; In order to thoroughly test your elevator, it is a
  good idea
  to run each test multiple times with different random seeds, but how
  much
  testing you do is up to you.&nbsp; <br>
  <br>
  To meet the minimum requirements, you must execute (and pass) each test
  one
  time.&nbsp; If your design fails to pass the test with the random seed
  chosen
  by the TAs, you will lose some points, even if the tests passed with
  the random
  seed you chose.<br>
  <br>
  If you run the tests multiple times, you should create a separate entry
  in the
  Acceptance Test Log for each one.&nbsp; You can differentiate these
  tests by
  putting the random seed used in the "Random Seed" column of the
  Acceptance Test Log.<br>
  <br>
  For each bug that you find and fix during acceptance testing, you must
  add an
  entry to the issue log.<br>
  </p>
  <h2>2. Begin Fast Elevator Design</h2>
  <p>From this project forward, your elevator shall meet the following
  additional
  high level requirements.&nbsp; <br>
  </p>
  <ul>
  <li> R-T6: The Car shall only stop at Floors for which there are
  pending calls. </li>
  <li> R-T7: The Car shall only open Doors at Hallways for which there
  are
  pending calls.</li>
  <li> R-T8: The Car Lanterns shall be use in a way that does not
  confuse
  passengers.
  <ul>
  <li>R-T8.1: If any door is open at a hallway and there are any
  pending calls at
  any other floor(s), a Car Lantern shall turn on.</li>
  <li>R-T8.2: If one of the car lanterns is lit, the direction
  indicated shall
  not change while the doors are open.</li>
  <li>R-T8.3: If one of the car lanterns is lit, the car shall
  service any calls
  in that direction first. </li>
  </ul>
  </li>
  <li> R-T9: The Drive shall be commanded to fast speed to the maximum
  degree
  practicable. </li>
  <li>R-T10: For each stop at a floor, at least one door reversal shall
  have occured before the doors are commanded to nudge.</li>
  </ul>
  <p>Add these new requirements to the existing high level requirements
  in the <span style="color: rgb(0, 153, 0);">Requirements I </span>document.</p>
  <p>Note:&nbsp; These high level requirements may conflict with some of
  the
  simple controller behaviors provided in the first half of the
  course.&nbsp; If
  you have been using those behaviors up to this point, you will need to
  modify
  them in order to meet these new requirements.<br>
  </p>
  <p>Note 2:&nbsp; If you need to, you can refer back to the&nbsp;
  <a
  href="http://www.ece.cmu.edu/%7Eece649/project/proj3/proj3_drive_acceleration_profile.html">the
  drive
  acceleration
  profile
  from project 3.</a><br>
  </p>
  <h3>Optional Modifications to Network Interfaces</h3>
  <p>In order to facilitate more sophisticated and optimized dispatcher
  behaviors, you may make the following changes for this project or in
  any future
  project (when you are ready to do more optimization).&nbsp; Note that
  these
  changes <span style="font-weight: bold;">are not required</span>.&nbsp;
  You
  should
  be
  able to fully meet all high level requirements with the
  existing
  interfaces.&nbsp;&nbsp; </p>
  <ul>
  <li>You may make <span style="font-weight: bold;">one and only one</span>
  of
  the following modifications to the input interface.:</li>
  </ul>
  <ol>
  <li>Add mCarPositionIndicator to the input of the Dispatcher and
  DriveControl,
  OR<br>
  </li>
  <li>Add mDriveSpeed and mCarLevelPosition to the input of the
  Dispatcher.</li>
  </ol>
  <ul>
  <li>If you wish to make any modification other than one of the ones
  listed
  above, you must first obtain approval from one of the TAs.&nbsp; In
  that case,
  be sure to ask the TA how to document the change in your portfolio.</li>
  </ul>
  <p>If you choose to change the make one of the above modifications, you
  must
  document the change in the following ways:<br>
  </p>
  <ul>
  <li>Add an entry to the issue log describing the changes and the
  items affected
  by the change.</li>
  </ul>
  <ul>
  <li>This change originates in the architecture, since the message
  interface is
  part of the architecture.</li>
  <li>You should list all affected artifacts (even statecharts and
      code), even if
  those artifacts won't be updated until a later project.<br>
  </li>
  </ul>
  <ul>
  <li>Update the input messages in the input interface for the affected
  controllers in the <span style="color: rgb(0, 102, 0);">Requirements
  II </span>document to reflect the change.&nbsp; <br>
  </li>
  <li>In the input interface section for the affected controllers, add
  a note
  that explains the purpose of the change and what behaviors it will
  enable.&nbsp; </li>
  </ul>
  <p>You should spend some time thinking about the implications of these
  changes
  and what optimized dispatcher behavior they might
  enable.&nbsp;&nbsp;&nbsp; You
  are not required to make a change to the interfaces at all, and you may
  make a
  change at any time during the rest of the semester (as long as you
      fully
      document the change as described here).&nbsp; Since you are already
  updating
  the design, you will likely save yourself some work by making the
  change sooner
  rather than later.<br>
  </p>
  <h3>A <span style="font-style: italic;">Recommendation </span>About
  Design
  Verification</h3>
  <p>Runtime monitoring was introduces in Project 7.&nbsp; In Project 11,
  you
  will use runtime monitoring to verify that your design meets the new
  high-level
  requirements.&nbsp; Although you are not required to do so, you should
  look
  ahead at the monitoring requirements and think about how you can use
  monitoring
  to verify your improved design.&nbsp; We strongly recommend that you
  start
  using a monitor for verification as soon as you have a complete
  design.&nbsp;
  Dispatcher algorithms are very complex and have many corner
  cases.&nbsp; You
  probably won't find all the problems without runtime monitoring.&nbsp;
  You
  should also consider generating additional acceptance tests to further
  exercise
  your design.&nbsp; Again, these are not requirements, but remember that
  the
  sooner you identify problems in your design, the easier they are to fix!<br>
  </p>
  <h2>3. Sequence diagrams for Fast Elevator design<br>
  </h2>
  <p>To meet the new high-level requirements, you will need to update
  your
  design, starting with your scenarios and sequence diagrams.&nbsp; You
  will
  update the scenarios and sequence diagrams you have already written and
  add new
  ones as needed.&nbsp; You should modify or add sequence diagrams to
  address all
  the new high-level requirements.&nbsp; <span style="font-weight: bold;">Each
  new
  behavior
  (high
   level requirement) must show up in at least one sequence
  diagram.&nbsp; </span><br>
  </p>
  <p>Follow all the guidelines for scenarios and sequence diagrams from
  Project
  2, including:<br>
  </p>
  <ul>
  <li>the formatting conventions for arcs and diagrams</li>
  <li>naming conventions for physical/network message arcs</li>
  <li>conducting peer reviews</li>
  </ul>
  <p></p>
  <p><b>Some thoughts on adding scenarios</b><br>
  Each scenario must originate from a valid use case.&nbsp; Note that the
  sections in the scenarios and sequence diagrams (2.&nbsp; Passenger
      Makes a Car
      Call, 3. Passenger Enters Car, etc) correspond to use cases.&nbsp; They
  are <span style="font-weight: bold;">not </span>just section
  headings.&nbsp; Any
  scenario/sequence diagram you add must be added to the Scenarios and
  Sequence
  diagrams document in the the section corresponding to the appropriate
  use
  case.&nbsp; This is a form of forward traceability.<br>
  </p>
  <p>It is likely that the existing use cases are adequate to cover the
  new
  behaviors., but you may create new use cases if you wish.&nbsp; You
  should
  avoid creating use cases that duplicate the existing use cases.&nbsp;
  For
  example, adding a use case for "Passenger rides in the elevator"
  would be redundant because that use case is covered by use cases
  1-7.&nbsp;&nbsp;&nbsp; If you choose to create new use cases, you will
  need to
  update the use case diagram (and log the change in your issue log) so
  that your
  design continues to be complete and consistent.<br>
  </p>
  <p><span style="font-weight: bold;">Sequence diagram numbering</span><br>
  Since you will be modifying your sequence diagrams, it is acceptable to
  add
  arcs that are not numbered contiguously with the arcs already in place
  and to
  remove an arc without renumbering the arcs.&nbsp; This will simplify
  the task
  of updating traceability and reduce the chance of introducing
  traceability
  errors that might be caused by a complete renumbering of arcs.&nbsp; <br>
  </p>
  <p>You must make sure your traceability is updated to reflect any arcs
  that are
  added or removed from sequence diagrams.<br>
  </p>
  <p>Note that the arc numbering must still correspond to the correct
  step in the
  scenario (e.g. if you add an arc that goes in the 4th step, it must
      still be
      numbered something like "4h").&nbsp; You can see some examples of
  this in the sodamachine example portfolio.<br>
  </p>
  <p><span style="font-weight: bold;">Issue log entries</span><br>
  For each externally observable change in the elevator behavior, include
  an
  issue log entry describing the modification to the elevator's behaviors
  and
  listing the sequence diagrams that are affected by the change,
  including
  sequence diagrams that you added to address the new behavior.<br>
  <br>
  </p>
  <div>
  <span style="font-weight: bold;">Annotate sequence diagram table of
  contents </span>&nbsp; <br>
  After you have completed your sequence diagrams, make sure the table of
  contents in the Scenarios and Sequence Diagrams document is complete
  and
  up-to-date (with working hyperlinks).&nbsp; Then add an annotation
  describing
  which of the new high-level behaviors (if any) is addressed in that
  sequence
  diagram.&nbsp; For example, if sequence diagrams 8A and 8B include
  elevator
  motion where the elevator reaches fast speed, then the table of
  contents entry
  for the sequence diagram would be similar to:<br>
  <ul>
  <li>Scenario 8A - R-T9</li>
  <li>Scenario 8B - R-T9</li>
  </ul>
  <span style="font-style: italic;">Note:&nbsp; If the above items were
  really in
  your Scenarios and Sequence Diagrams document table of contents, the
  text
  "Scenario 8A" and "Scenario 8B" would contain a link that
  points to an anchor tag at the start of each Scenario.</span><br>
  <br>
  This is a lightweight traceability task designed to help the graders
  more
  easily determine whether you have included all the new behaviors in
  your
  sequence diagrams.&nbsp; You only need to trace the new behaviors R-T6
  through
  R-T10, but you should separately trace the subnumbered requirements
  R-T8.1
  through R-T8.3.&nbsp; <span style="font-weight: bold;">Each new high
  level
  requirement must be traced to to at least one sequence diagram.</span><br>
  <h2>4.&nbsp; Time-Triggered Requirements for Fast Elevator Design</h2>
  After you update your sequence diagrams, you will also update your
  time-triggered requirements to encompass the new behaviors described in
  your
  updated sequence diagrams.&nbsp; Look back at Project 4 and review the
  guidelines for time-triggered requirements.&nbsp; You must continue to
  follow
  all of those guidelines as you update your design.&nbsp; You may not
  modify the
  constraints listed in the Requirements II document.<br>
  <br>
  Since you will not be updating your event-triggered requirements, if
  they are
  still listed in your Requirements II portfolio document, you should
  remove them
  now to avoid confusion and ambiguity.<br>
  <h2>5.&nbsp; Traceability<br>
  </h2>
  <p>Since you have updated your sequence diagrams and requirements, you
  should
  also update the Requirements-to-Sequence-Diagrams and
  Requirements-to-Constraints traceability tables.<br>
  </p>
  <p>Since you have not updated the statecharts to conform to the new
  behaviors,
  it is acceptable for the Requirements-to-Statecharts tables to be
  inaccurate <span style="font-weight: bold;">for this project handin
  only</span>.&nbsp; You
  will have to update them next week when you update your state charts.<br>
  </p>
  <p>If you find and fix bugs related to the acceptance testing in step
  1, you
  should continue to log these bugs and update the design and
  traceability
  related to those problems.&nbsp; For example, an acceptance test bug
  requires
  you to change a the guard condition on statechart and the related
  code.&nbsp;
  There should be an issue log entry for this bug. &nbsp; You should also
  update
  the statechart-to-requirements and statechart-to-code to be consistent
  with the
  changes.&nbsp; <br>
  </p>
  <p>It is possible (but not likely if you have a clean design) that bug
  fixes
  related to acceptance testing will interact with the fast elevator
  design
  changes.&nbsp; In that case, you should think carefully about how this
  bug
  might manifest in the new design and what changes you might make to the
  design
  accordingly.&nbsp; If the changes to the design related to the fast
  elevator
  behaviors are in direct conflict with the bug fix changes from
  acceptance
  testing (e.g. the fast elevator requires one behavior and the bug fix
      for the
      old design requires a completely different, incompatible behavior),
  then you
  will need to make the bug fix changes in order to pass acceptance
  tests.&nbsp;
  Document the conflicting fast elevator design changes as an unresolved
  issue in
  the issue and add them next week.<br>
  </p>
  <p>Keeping track of changes from two sources (new high level
      requirements and
      acceptance testing) can be challenging, so you will need to make a
  special
  effort to coordinate with your teammates when executing the various
  parts of
  this project.<br>
  <br>
  </p>
  </div>
  <h2>6.&nbsp; Peer Reviews</h2>
  <p>Pick the 4 scenarios and corresponding sequence diagrams that have
  changed
  the most for your fast elevator, and perform peer reviews on them.
  (Teams of 3
   can pick only 3 if desired.) We suggest you peer review more items, but
  this is
  the minimum.</p>
  <p>Pick the 4 sets of time triggered requirements that changed the most
  for
  your fast elevator, and perform peer reviews on them. (Teams of 3 can
      pick only
      3 if desired.)</p>
  <p>Any reasonable interpretation of "changed the most" is fine, but
  we're willing to bet the dispatcher is going to be one of them.
  Entirely new
  items have also "changed" for our purposes, so most gropus will end
  up picking the most complex four new scenarios and sequence diagrams to
  review,
  as well has new behaviors for the dispatcher and some other components.</p>
  <hr>
  <h2 style=""> Team Design Portfolio </h2>
  <p> The portfolio you submit should contain the most up-to-date design
  package
  for your elevator system organized and formatted according to the
  <a
  href="http://www.ece.cmu.edu/%7Eece649/project/portfolio/portfolio_layout.html">portfolio
  guidelines</a>.<span style="">&nbsp;</span> You are going to update
  your
  portfolio every week, so be sure to keep an up to date working
  copy.&nbsp;<br>
  </p>
  <h4> Ensure your design portfolio is complete and consistent. </h4>
  <p>The following is a partial list of the characteristics your
  portfolio should
  exhibit: </p>
  <ul>
  <li>Changes requested by the TAs in previous projects have been
  applied. </li>
  <li>All required documents are complete and up-to-date to the extent
  required
  by the projects (you do not need to update files or links related to
      future
      projects).<br>
  </li>
  <li>All documents include group # and member names at the top of the
  document.&nbsp; (This includes code, where this information should
      appear in
      the header field) </li>
  <li>Individual documents have a uniform appearance (i.e., don't look
      like they
      were written by 4 individual people and then pieced together) </li>
  <li>The issue log is up to date and detailed enough to track changes
  to the
  project. </li>
  </ul>
  <p><b></b> </p>
  <hr>
  <h2> Handing In Results</h2>
  <ul>
  <li> Each team shall submit exactly one copy of the assignment.<br>
  </li>
  <li>Follow the handin instructions detailed in the Project FAQ to
  submit your
  portfolio into the afs handin directory ( <tt>/afs/ece/class/ece649/Public/handin/project8/group#/ontime/).</tt>
  <span style="font-weight: bold;"><br>
  </span></li>
  <li><span style="font-weight: bold;">Be sure you follow the required
  format for
  the directory structure of the portfolio and its location in the handin
  directories.</span><tt></tt><br>
  </li>
  <li>Be sure to follow <span style="font-weight: bold;">ALL</span>
  the portfolio
  guidelines detailed in the <a
  href="http://www.ece.cmu.edu/%7Eece649/project/portfolio/portfolio_layout.html">Portfolio
  Layout</a> page.<b><br>
  </b></li>
  <li><b>Any submission that contains files with modification dates
  after the
  project deadline will be considered late and subject to a grade
  deduction</b>
  (see <a href="http://www.ece.cmu.edu/%7Eece649/admin.html#grading">course
   policy
   page</a> for more information). </li>
  </ul>
  <hr style="width: 100%; height: 2px;">
  <h2>Grading Criteria:</h2>
  <p>This assignment counts as one team grade. If you choose to divide
  the work,
  remember that you will be graded on the whole assignment.&nbsp;
<a
href="http://www.ece.cmu.edu/%7Eece649/project/proj8/proj8_grade_sheet.pdf">A
detailed
grading
rubric
is available here.</a><br>
<span style="font-weight: bold;">Project 8 is worth 125 points:</span> </p>
<ul>
<li><span style="font-weight: bold;">30 points </span>for complete
unit,
  integration, and acceptance testing</li>
  <li><span style="font-weight: bold;">10 points </span>for writing
  and running
  your own acceptance test<br>
  </li>
  <li><span style="font-weight: bold;"> 30 points</span> for updated
  scenarios
  and sequence diagrams</li>
  <li><span style="font-weight: bold;"> 30 points</span> for updated
  time
  triggered requirements</li>
  <li><span style="font-weight: bold;"> 10 points</span> for peer
  reviews of
  new/modified scenarios and sequence diagrams</li>
  <li><span style="font-weight: bold;"> 10 points</span> for peer
  reviews of
  new/modified time triggered requirements</li>
  <li><span style="font-weight: bold;">5 points</span> for an entry in
  the <span style="font-weight: bold;">Improvements Log </span>that
  tells us what can
  be improved about this project. If you encountered any minor bugs that
  we
  haven't already addressed, please mention them so we can fix them. If
  you have
  no suggestions, say so in your entry for this project.</li>
  </ul>
  <p>In addition to points explicitly allocated to traceability and
  portfolio
  formatting, we may also make random checks and deduct points if we find
  that
  you do not submit a complete and consistent design package.<br>
  <br>
  If you choose to work ahead and go beyond the requirements for this
  project,
  that is fine.&nbsp; As with previous project phases, you must meet all
  the
  stated project requirements for this phase regardless of how much extra
  work
  you do. </p>
  <hr>
  <p>Back to <a href="http://www.ece.cmu.edu/%7Eece649/">course home page</a>
  </p>
  </body>
  </html>
