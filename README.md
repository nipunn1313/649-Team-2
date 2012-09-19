649-Team-2
==========

Elevator project for 18-649.

Project 3 Requirements

<ol>
  <li>Use the Scenarios and Sequence Diagrams from project 2 to
  generate your
  behavioral requirements for each control system object. We've provided
  some examples, but again, these are sub-optimal so you may want to
  write your own. Follow the format of the "formula for behavioral
  requirements" for an <span
   style="font-style: italic; font-weight: bold;">event-based system</span>
   you'll
   see in multiple lectures. A particularly important concern is that <span
    style="font-style: italic;">only
	one</span> message can be used as the trigger for an action. If you
	need two
	messages to trigger an action, you will generally need to use multiple
	behavioral requirements and intermediate variables to do this.</li>
	</ol>
	<ul>
	  <ul>
	      <li>Each requirement should be less than 50 words, and all but the
		  most
		  complex should be less than 25 words. (If you have a requirement
		  greater
		  than 25 words long, then consider breaking it up into simpler
		  requirements, perhaps using nested levels of numbering). <br>
		      </li>
			      <li>Each
				  requirement <span style="font-weight: bold;">shall </span>be less
				  than 100 words and <span style="font-weight: bold;">shall </span>be a
				  legitimate
				  English sentence. Only the first 100 words of any numbered requirement
				  will be graded. Hyphens and equal signs both count as spaces when
				  determining word count.&nbsp;</li>
				      <li>Each requirement <span style="font-weight: bold;">shall </span>contain
					  exactly
					  one
					  verb.&nbsp;
					  This

					  will
					  likely
					  lead to multipart requirements
					  when the same IF part results in multiple THEN parts.&nbsp; In the
					  example that follows, R1.1a and R1.1b are considered to be separate (but
					  related) requirements that each include the common text stated in the
					  R1.1 line.&nbsp; So the word count for R1.1a is 7+8=15 words
					  and R1.1b is 7+6=13 words. <br>
					      </li>
						      <ul>
							        <li>R1.1&nbsp; If mMessageX[f,b] is received as true,
									then&nbsp;&nbsp; (7 words)<br>
									      </li>
										        <ul>
												        <li>R1.1a&nbsp; State variable A shall be set to
														True.&nbsp;&nbsp;&nbsp; (8 words)<br>
														        </li>
																        <li>R1.1b&nbsp; OutputY shall be set to
																		False.&nbsp;&nbsp;&nbsp; (6 words)<br>
																		        </li>
																				      </ul>
																					      </ul>
																						    </ul>
																							</ul>
																							<ol start="2">
																							  <li>Ensure traceability by completing the Sequence Diagrams to
																							  Requirements Traceability table.&nbsp; Expand the Excel template in the
																							  portfolio to include a row for each requirement and a column for each
																							  sequence diagram arc.&nbsp; Be sure to follow the instructions given in
																							  the notes page of the Excel template.&nbsp; <br>
																							    </li>
																								  <ol>
																								      <li>Each behavior should match
																									  up
																									  with one or more Sequence Diagram messages (complete backward
																									  traceability).</li>
																									      <li>Each Sequence Diagram
																										  message
																										  should apply to at least one text behavior requirement (complete
																										  forward traceability).&nbsp;&nbsp;</li>
																										      <li>When tracing to multipart requirements (as in the example in #1
																											  above), you should trace to each subrequirement (with the understanding
																											  that it includes the common text).&nbsp; So in example, you would trace
																											  to R1.1a and R1.1b, but NOT R1.1 by itself.<span
																											   style="font-weight: bold;"><br>
																											         </span></li>
																													     <li>There can
																														 be as many overlaps as necessary as long as every Sequence Diagram
																														 message and requirement is
																														 covered.&nbsp; <br>
																														     </li>
																															     <li>If a requirement only sets a state variable and does not set
																																 any
																																 output, then the requirement will not trace to any sequence diagram
																																 arc.&nbsp; In this case, it is acceptable to trace it to the "Setting
																																 State Variable" column in the traceability table.</li>
																																     <li>Some arcs in the sequence diagram will originate from system
																																	 modules (like the smart sensors).&nbsp; Since you do not have
																																	 requirements for these objects, just put a single entry for each system
																																	 object in the System Modules section and indicate the arcs that trace
																																	 to that object.</li>
																																	     <li>If a requirement is related to a message that is not consumed
																																		 by any controller, you may trace it to the "Future Expansion"
																																		 column.&nbsp; In the elevator, the only messages that have no consumers
																																		 are the mHallLight and mCarLight messages.&nbsp; You may NOT trace a
																																		 requirement to "Future Expansion" UNLESS it is related to the
																																		 mHallLight of mCarLight messages.<br>
																																		     </li>
																																			     <li>Your traceability table must include all 7 of the
																																				 controllers.&nbsp; For the controllers that you did not write
																																				 requirements for (DoorControl,
																																				 CarPositionControl and
																																				 Dispatcher), you should trace to the requirements that have been
																																				 provided.</li>
																																				     <li>You must have a team member DIFFERENT from the author of the
																																					 behavioral requirements perform the traceability check on each Sequence
																																					 Diagram. The team member who performs the check for each object should
																																					 record his/her
																																					 name on the traceability check.&nbsp; <b><i>Complete and consistent
																																					 traceability between your
																																					 diagrams and documentation will be a major factor in your grade.<br>
																																					       <br>
																																						         <br>
																																								       </i></b></li>
																																									     </ol>
																																										   <li> You must also ensure traceability between the Requirements and
																																										   Constraints for each object you develop requirements for (<span style="color: rgb(0, 153, 0);">traceability/req_const_traceability.html</span>). For a single
																																										   object, only trace to the Constraints of that object, not the
																																										   Constraints for all objects. This is easily done using a table with
																																										   the
																																										   Constraints listed across the top and your Requirements listed along
																																										   the
																																										   side. You don't have to provide a detailed explanation - just use an
																																										   'X' if the Requirement directly supports the Constraint, and a '~' if
																																										   the Requirement doesn't contradict the Constraint but doesn't directly
																																										   support it. Turn in a Constraint to Requirement traceability check for
																																										   each object you are responsible for writing requirements for. You will
																																										   probably have at least one 'X' per column and at least one 'X' per
																																										   row.&nbsp; The team member who performs the check for each object
																																										   should record his/her
																																										   name on the traceability check.<br>
																																										       <br>
																																											   You should perform this traceability exercise for all 7 of the
																																											   controllers.&nbsp; For the controllers that you did not write
																																											   requirements
																																											   for (DoorControl,
																																											   CarPositionControl and
																																											   Dispatcher), you should trace to the requirements that have been
																																											   provided.<br>
																																											       <br>
																																												       <br>
																																													     </li>
																																														   <li>Issue Log - As you are working on requirements, you will likely
																																														   find errors or omissions in your sequence diagrams.&nbsp; If that
																																														   happens, you must update your sequence diagrams so that they are
																																														   consistent, and track each modification in your issue log.&nbsp;
																																														   Finding bugs is not a bad thing!&nbsp; In fact, if a team is not
																																														   finding bugs, it is much more likely that they are not looking hard
																																														   enough than that there are no bugs to be found.&nbsp; It is possible
																																														   (but not likely) that you will not have any errors in your sequence
																																														   diagrams, so there are no points assigned for this part.&nbsp; But keep
																																														   in mind that we can go back and compare your current sequence diagrams
																																														   to previous weeks, and we will expect modifications to be logged, and
																																														   you might have points deducted if it is clear you are not making a good
																																														   faith effort to track bugs in your project.<br>
																																														       <br>
																																															       <br>
																																																     </li>
																																																	   <li>Peer Review Log - You must complete a peer review checklist for
																																																	   each set of requirements ie. one peer review per object. Record the
																																																	   results in a <a
																																																	    href="http://www.ece.cmu.edu/%7Eece649/project/misc/peer_review.xls">peer
																																																		review
																																																		sheet</a> and also complete a log entry into peer review log.
																																																		Recording defects on the peer review sheet does NOT result in point
																																																		deductions. Be honest. We just want to know if you found something or
																																																		not in the review. If you find something, you should fix it before
																																																		handing in the assignment or else note it in the issue log as an open issue.<br>
																																																		    <br>
																																																			  </li>
																																																			  </ol>
