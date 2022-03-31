# BdmSimulator
This simulates enhancements in Black Desert Mobile.  Input how many resources (pristine crystals, valks, resto scrolls) you have and what enhancement settings
(e.g. what stage to start valks and resto scrolls, what your target stage is) you're planning on using, and the simulator will try it a whole bunch of times
(currently 500K) and tells you what your probability of succeeding is, as well as how much of your resources you're likely to use.

Input is via a HOCON file.  Examples are in base/input*.conf.

Output is similar to this:

    Config(SimpleConfigObject({"itemType":"GEAR","resources":{"crystal":{"limit":1336},"restoreScrolls":{"limit":240222,"startStage":4},"valks10"{"continueAfterExhausted":true,"limit":700,"startStage":1},"valks100":{"limit":19,"startStage":10},"valks50":{"limit":580,"startStage":3}},"startStage":5,"target":6})) probability of success = 0.994002
    crystal         exceeded= 0.00600 average used=    209.25 fraction below average= 0.642 median=    117  75%=    307  90%=    560  95%=    748  99%=   1187 
    valks10         exceeded= 0.00000 average used=    110.16 fraction below average= 0.644 median=     58  75%=    164  90%=    303  95%=    408  99%=    652 
    valks50         exceeded= 0.00000 average used=     74.24 fraction below average= 0.634 median=     45  75%=    108  90%=    190  95%=    253  99%=    390 
    valks100        exceeded= 0.00000 average used=      0.00 fraction below average= 0.000 median=      0  75%=      0  90%=      0  95%=      0  99%=      0 
    restoreScrolls  exceeded= 0.00000 average used=   6299.79 fraction below average= 0.635 median=   4000  75%=   9200  90%=  16000  95%=  21000  99%=  32000 
