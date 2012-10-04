" Syntax files for scripts used for 18-649
" Author: Nick Mazurek (mazurekn@gmail.com)

"if exists("b:current_syntax")
"  finish
"endif

" The types and contexts need to be capitalized, so we need to match case
syn match Number /\<\d*\>/
syn match ts /\<\d\+\.\?\d*m\?s\>/

syn match Comment /;\{1\}.*/
syn match spec_comment /;;.*/

" Defines and includes
syn match macro /#.*/

" Type: either Injection or Assertion
syn keyword type      I A

" Context, either framwork, network, or state
syn keyword context   F N S

" Accessor for member
syn keyword delim    :

syn keyword identifier  UP DOWN FRONT BACK LEFT RIGHT STOP SLOW LEVEL True False true false



" Highlighting
hi def link context   Label
hi def link macro     Include
hi def link ts        Number 
hi def link type      Keyword
hi def link delim     Special
hi def link spec_comment  Macro
hi def link identifier  Constant

let b:current_syntax = "649"
