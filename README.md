649-Team-2
==========

Elevator project for 18-649.

<html>
  <body>
    The procedures and requirements for this project are identical to those of
    project 5; you'll just be implementing and testing different controllers. So,
    all the requirements from project 5 and the Testing Requirements document apply
    to this project as well.<br>
    <ol>
      <li>Make sure you have the latest version of the simulator code
      from
      the <a href="../codebase/download.html">download page</a>. <br>
      <br>
      </li>
      <li> Implement (in Java) the CarPositionControl, Dispatcher, and
      LanternControl objects using your statecharts.&nbsp; All the
      rules and
      procedures for implementing these objects are the same as they
      were for
      implementing the other objects in project 5. <br>
      <br>
      </li>
      <li>Update statecharts to code traceability to include the new
      objects.<br>
      <br>
      </li>
      <li>Write complete unit tests to informally test every state and
      transition
      of the CarPositionControl, Dispatcher, and
      LanternControl statecharts, like the unit tests you created
      for the DriveControl in project 5.&nbsp;&nbsp;<span
        style="font-weight: bold;"></span></li>
      <ul>
        <li>After you have written the new tests, execute all the unit
        tests from Project 5 and 6 and log the results in the test
        log.</li>
        <li><span style="font-weight: bold;">Note:&nbsp; For this
          project, your design MUST pass all unit tests for all seven
          controllers
          (the three from this project and the four from project 5).</span><br>
        <br>
        </li>
      </ul>
      <li>Write and execute additional integration tests to informally
      test <span style="font-weight: bold;">a total of ten (10) </span>of
      your
      sequence
      diagrams<br>
      </li>
      <ul>
        <li>The ten tests may include the test your created in Project
        5.</li>
        <li>The ten tests MUST include the sequence diagrams for
        scenarios 4A, 5B, 6A, and 9A.</li>
        <li>Complete all the steps outlined in Project 5 for integration
        tests.</li>
        <li><span style="font-weight: bold;">Note:&nbsp; For this
          project, your design MUST pass eight out of ten of the
          integration tests (8/10).<br>
        </span></li>
      </ul>
    </ol>
    <br>
    <br>
    <hr>
    <h2>Team Design Portfolio </h2>
    <p>
    The portfolio you submit should contain the most up-to-date design
    package for your elevator system organized into the following
    directories.<span style="">&nbsp; </span>You are going to update
    your
    portfolio every week, so be sure to keep an up to date working
    copy.&nbsp; <br>
    </p>
    <p class="MsoNormal">Files that you should update for this week are:<br>
    </p>
    <ul>
      <li>Portfolio Table of Contents<br>
      </li>
      <li>Scenarios and Sequence Diagrams</li>
      <li>Improvements Log</li>
      <li>Requirements II</li>
      <li>Sequence Diagrams to Requirements Traceability<br>
      </li>
      <li>Requirements to Constraints Traceability</li>
      <li>Issue Log</li>
      <li>Statechart to Code Traceability</li>
      <li>Elevator Control Package (be sure to include a complete,
      compilable submission in the elevatorcontrol folder)<br>
      </li>
      <li>Unit and Integration Test Logs (be sure to include
      all relevant input and output files from the tests)</li>
    </ul>
    <p class="MsoNormal"> </p>
    <span style=""></span>
    <h4>Ensure your design portfolio is complete and consistent. </h4>
    The following is a partial list of the characteristics your
    portfolio should exhibit:
    <ul>
      <li>Changes requested by the TAs in previous projects have been
      applied.</li>
      <li>All required documents are complete and up-to-date to the
      extent required by the projects (you do not need to update files
      or
      links related to future projects).<br>
      </li>
      <li>All documents include group # and member names at the top of
      the
      document.&nbsp; (This includes code, where this information
      should
      appear in the header field) </li>
      <li>Individual documents have a uniform appearance (i.e., don't
      look
      like they were written by 4 individual people and then pieced
      together)</li>
      <li>The issue log is up to date and detailed enough to track
      changes
      to the project.</li>
    </ul>
    <hr style="width: 100%; height: 2px;">
    <h2> Handing In Results</h2>
    <p class="MsoNormal"> Each team shall submit exactly one copy of the
    assignment.<br>
    </p>
    <p class="MsoNormal">Follow the handin instructions detailed in the
    Project FAQ to submit your portfolio into the afs handin directory
    (<tt>/afs/ece/class/ece649/Public/handin/project6/group#/ontime/).&nbsp;
      </tt><span style="font-weight: bold;"><br>
    </span></p>
    <p class="MsoNormal"><span style="font-weight: bold;">Be sure you
      follow the required format for the directory structure of the
      portfolio
      and its location in the handin directories.</span><tt><br>
    </tt></p>
    <p>Be sure to follow <span style="font-weight: bold;">ALL</span>
    the
    portfolio guidelines detailed in the <a
      href="../portfolio/portfolio_layout.html">Portfolio Layout</a>
    page.<br>
    </p>
    <p><b>Any submission that contains files with modification dates
      after the project deadline will be considered late and subject
      to a
      grade deduction</b> (see <a
      href="http://www.ece.cmu.edu/%7Eece649/admin.html#grading">course
      policy
      page</a> for more information). </p>
    <hr style="width: 100%; height: 2px;">
    <h2>Grading (160 points):</h2>
    <p>This assignment counts as one team grade. If you choose to divide
    the work, remember that you will be graded on the whole
    assignment.&nbsp; <br>
    </p>
    <p><span style="font-weight: bold;"></span>General
    grading will be as follows.&nbsp; <a href="proj6_grade_sheet.pdf">A
      detailed grading rubric is
      available here</a>.&nbsp; <span style="font-weight: bold;">Pay
      special
      attention that you submit code
      that will compile correctly with the current version of the
      simultor.&nbsp; You stand to lose at least 23% of the grade if
      your
      code will not compile!</span><br>
    <br>
    <span style="font-weight: bold;">25 points </span>- for the
    implementations of the CarPositionControl, Dispatcher, and
    LanternControl.<br>
    <span style="font-weight: bold;">30 points </span>- for
    statecharts-to-code traceability<br>
    <span style="font-weight: bold;">40 points </span>- for complete
    unit
    testing, unit test log, and passing all unit tests<br>
    <span style="font-weight: bold;">30 points </span>- for
    completing 10
    integration tests, and passing 8 of them.<br>
    <span style="font-weight: bold;">30 points</span> - for completing
    14
    peer reviews (8 Integration tests, 3 Unit Tests, 3 Code)<br>
    <span style="font-weight: bold;">5 points </span>- For an entry
    in the
    Improvements Log that tells us what can be improved about this
    project.
    If you encountered any minor bugs that we haven't already
    addressed,
    please mention them so we can fix them. If you have no
    suggestions, say
    so in your entry for this project.<br>
    </p>
    <b>NOTE: </b> To receive full credit for the assignment the test
    files you create must be formatted correctly and fully commented to
    explain what they are doing.
    <hr>
    <p> Back to <a href="http://www.ece.cmu.edu/%7Eece649/index.html">course
      home
      page</a> </p>
  </body>
</html>
