649-Team-2
==========

Elevator project for 18-649.

<html>
<body>
<br>
<center>
  <h1> 18-649 Project 9</h1>
  </center>
  <div style="text-align: center;">
  </div>
  <center>
  <div style="text-align: left;">
  <div style="text-align: center;">
  <h2><b>Advanced Elevator and Smart Dispatcher Design</b></h2>
  <span style="font-weight: bold;">Check the <a
   href="http://www.ece.cmu.edu/%7Eece649/">course webpage</a> for due
  dates</span><br>
  <span style="font-weight: bold;"></span><br>
  </div>
  <span style="font-weight: bold;"></span>Please submit all
  project-related correspondence to&nbsp;<img
   style="width: 295px; height: 22px;" alt="staff email address"
   src="../e-mail_addr.gif"><br>
  </div>
  </center>
  </center>
  <hr style="width: 100%; height: 2px;">Changelog:<br>
  <ul>
  </ul>
  <ul>
    <li>Dated entries will appear here if the assignment changes after it
  is released. </li>
  </ul>
  <ul>
  </ul>
  <hr style="width: 100%; height: 2px;">
  <h2>A <span style="font-style: italic;">Recommendation </span>About
  Design Verification</h2>
  Runtime monitoring was introduces in Project 7.&nbsp; In Project 11,
  you
  will use runtime monitoring to verify that your design meets the new
  high-level requirements.&nbsp; Although you are not required to do so,
  you
  should look ahead at the monitoring requirements and think about how
  you can use monitoring to verify your improved design.&nbsp; We
  strongly
  recommend that you start using a monitor for verification as soon as
  you have a complete design.&nbsp; Dispatcher algorithms are very
  complex and
  have many corner cases.&nbsp; You probably won't find all the problems
  without runtime monitoring.&nbsp; You should also consider generating
  additional acceptance tests to further exercise your design.&nbsp;
  Again,
  these are not requirements, but remember that the sooner you identify
  problems in your design, the easier they are to fix!<br>
  <h1>Assignment</h1>
  Now that you've finished the
  sequence diagrams and requirements for your elevator
  control
  system, you'll make the design changes through the rest of your design
  documents.<br>
  <ol>
    <li><span style="font-weight: bold;">Update statecharts</span> - You
  will update your statecharts based on
  the changes and additions made to your requirements in Project 8.&nbsp;
  Your statecharts must continue to be pure time-triggered design.&nbsp;
  You should refresh your memory by rereading the time-triggered design
  guidelines in <a href="../proj4/index.html">Project 4</a>.</li>
    <li><span style="font-weight: bold;">Update unit tests</span> - You
  will update your unit tests so that they
  correspond to the changes made in your statecharts.&nbsp; <br>
    </li>
    <ul>
      <li>Your tests must be syntactically valid (must execute without
  causing any runtime exceptions in the simulator), but you are <span
   style="font-weight: bold;">not</span> required to pass unit tests this
  week (OK to have failed assertions).&nbsp; You will be required to pass
  them next week, so don't blow
  off the creation of unit tests.&nbsp;&nbsp;</li>
      <li>Your <span style="color: rgb(0, 102, 0);">Unit Test Log</span>
  must be complete and up-to-date, including describing the issues
  related to any controllers that are not passing all their unit tests.<br>
      </li>
      <li>Make sure your <span style="color: rgb(0, 102, 0);">Unit Test
  Summary File</span> is up-to-date, as this will be used to automate the
  grading of your portfolio.<br>
      </li>
    </ul>
    <li><span style="font-weight: bold;">Update implementation (code) </span>-
  You will revise your controllers to implement the new statecharts that
  you have created.&nbsp; You must continue to follow the controller guidelines
  outlined in <a href="../proj5/index.html">Project 5</a>.</li>
    <li><span style="font-weight: bold;">Complete End-to-End Traceability
      </span>- Since your design update will be completed by the
  time you turn in this project, you must also have update and complete
  end-to-end traceability, including:</li>
    <ul>
      <li>Sequence Diagrams to Requirements traceability</li>
      <li>Requirements to Constraints traceability</li>
      <li>Requirements to Statecharts traceability (tables in the <span
   style="color: rgb(0, 102, 0);">Requirements II </span>document).</li>
      <li>Statecharts to Code traceability</li>
      <li>Complete and correct state and arc traceability comments in
  your unit tests, as described in <a href="../proj5/index.html">Project
  5</a>.</li>
    </ul>
    <li><span style="font-weight: bold;">Peer Review -&nbsp;</span></li>
    <ul>
      <li>You shall conduct a peer review for the following:</li>
      <ul>
        <li>Dispatcher Statechart</li>
        <li>DriveControl Statechart</li>
        <li>Dispatcher Code Implementation</li>
        <li>DriveControl Code Implementation</li>
      </ul>
      <li><span style="font-weight: bold;"></span>You should peer review
  any unit tests you revise.<br>
      </li>
    </ul>
  </ol>
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
  Project FAQ to submit your portfolio into the afs handin directory (<tt>/afs/ece/class/ece649/Public/handin/project9/group#/ontime/).</tt><span
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
  <hr style="width: 100%; height: 2px;">
  <h2>Grading Criteria:</h2>
  <p>This assignment counts as one team grade. If you choose to divide
  the work, remember that you will be graded on the whole assignment. <a
   href="proj9_grade_sheet.pdf">A detailed grading
  rubric is available here.</a><br>
  <span style="font-weight: bold;">Project 9 is worth 145 points:</span>
  </p>
  <ul>
    <li><span style="font-weight: bold;">20 points </span>for updated
  statecharts<br>
    </li>
    <li><span style="font-weight: bold;"> 20 points</span> for updated
  implementation</li>
    <li><span style="font-weight: bold;"> 20 points</span> for updated
  unit tests</li>
    <li><span style="font-weight: bold;">60 points </span>for complete
  traceability</li>
    <li><span style="font-weight: bold;">20 points </span>for peer
  reviews<br>
    </li>
    <li><span style="font-weight: bold;">5 points</span> for an entry in
  the <span style="font-weight: bold;">Improvements Log </span>that
  tells us what
  can be improved about this project. If you
  encountered any minor bugs that we haven't already addressed, please
  mention them so we can fix them. If you have no suggestions, say so in
  your entry for this project.</li>
  </ul>
  In addition to points
  explicitly allocated to traceability and portfolio formatting, we may
  also make
  random checks and deduct points if we find that you do not submit a
  complete and consistent design package.<br>
  <br>
  If you choose to work ahead and go beyond the requirements for this
  project, that is fine.&nbsp; As with previous project phases, you must
  meet all the
  stated
  project requirements for this phase regardless of how much extra work
  you do.
  <hr>
  <p>Back to <a href="http://www.ece.cmu.edu/%7Eece649/">course home page</a>
  </p>
  </body>
  </html>
