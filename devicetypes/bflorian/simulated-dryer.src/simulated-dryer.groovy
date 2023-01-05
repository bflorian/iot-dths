/**
 *  Simulated Washer
 *
 *  Copyright 2019 Bob Florian
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */
metadata {
	definition (name: "Simulated Dryer", namespace: "bflorian", author: "Bob Florian", cstHandler: true) {
		capability "Switch"
		capability "Dryer Operating State"
        capability "Ocf"
        
        attribute "timeRemaining", "number"
        command "setTimeRemaining", ["number"]
        command "start"
        command "stop"
	}

	tiles(scale: 2) {
		multiAttributeTile(name:"tottle", type: "lighting", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.machineState", key: "PRIMARY_CONTROL") {
				attributeState "run", label: '${name}', action: "stop", icon: "st.samsung.da.washer_ic_washer", backgroundColor: "#00A0DC"
				attributeState "pause", label: '${name}', action: "start", icon: "st.samsung.da.washer_ic_washer", backgroundColor: "#00A0DC"
				attributeState "stop", label: '${name}', action: "start", icon: "st.samsung.da.washer_ic_washer", backgroundColor: "#ffffff"
			}
		}
        controlTile("machineState", "device.machineState", "enum", width: 2, height: 2) {
            state("stop", action:"setMachineState", label: "Stop")
            state("pause", action:"setMachineState", label: "Pause")
            state("run", action:"setMachineState", label: "Run")
        }
        controlTile("timeRemaining", "device.timeRemaining", "slider", range: "0..120", width: 2, height: 2) {
            state "default", action:"setTimeRemaining"
        }
        standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00a0dc"
			state "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
		}
		
	}
}

def installed() {
	off()
    setMachineState('off')
    setTimeRemaining(120)
}

def updated() {
	off()
    setMachineState('off')
    setTimeRemaining(120)
}

def parse(String description) {
}

// handle commands
def on() {
	sendEvent(name: "switch", value: "on")
}

def off() {
	sendEvent(name: "switch", value: "on")
}

def start() {
	sendEvent(name: "machineState", value: "run")
   
  	def completionTime = device.currentValue('completionTime')
    log.debug("start, completionTime = ${completionTime.toSystemFormat()}")
	schedule(completionTime, timedTurnOff)
}

def stop() {
	sendEvent(name: "machineState", value: "stop")
}

def setMachineState(value) {
	sendEvent(name: "machineState", value: value)
}

def setTimeRemaining(remaining) {
	log.debug("remaining = $remaining")
	def completionTime = new Date(now() + (remaining * 60 * 1000))
    log.debug("completionTime = ${completionTime.toSystemFormat()}")
	sendEvent(name: "timeRemaining", value: remaining)
    sendEvent(name: "completionTime", value: completionTime.toSystemFormat())
    schedule(completionTime, timedTurnOff)
}

def timedTurnOff() {
	stop()
}