/**
 *  Simulated Vacuum
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
	definition (name: "Simulated Vacuum", namespace: "bflorian", author: "Bob Florian", cstHandler: true) {
		capability "Switch"
		capability "Robot Cleaner Cleaning Mode"
        capability "Ocf"
        
        attribute "lastMode", "string"
        command "resume"
        command "stop"
    }

	tiles(scale: 2) {
		multiAttributeTile(name:"tottle", type: "lighting", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.robotCleanerCleaningMode", key: "PRIMARY_CONTROL") {
				attributeState "auto", label: '${name}', action: "stop", icon: "st.samsung.da.RC_ic_rc", backgroundColor: "#00A0DC"
				attributeState "part", label: '${name}', action: "stop", icon: "st.samsung.da.RC_ic_rc", backgroundColor: "#00A0DC"
				attributeState "repeat", label: '${name}', action: "stop", icon: "st.samsung.da.RC_ic_rc", backgroundColor: "#00A0DC"
				attributeState "manual", label: '${name}', action: "stop", icon: "st.samsung.da.RC_ic_rc", backgroundColor: "#00A0DC"
				attributeState "map", label: '${name}', action: "stop", icon: "st.samsung.da.RC_ic_rc", backgroundColor: "#00A0DC"
				attributeState "stop", label: '${name}', action: "resume", icon: "st.samsung.da.RC_ic_rc", backgroundColor: "#ffffff"
			}
		}
       controlTile("machineState", "device.robotCleanerCleaningMode", "enum", width: 2, height: 2) {
            state "auto", label: 'Auto', action: "setRobotCleanerCleaningMode"
			state "part", label: 'Part', action: "setRobotCleanerCleaningMode"
			state "repeat", label: 'Repeat', action: "setRobotCleanerCleaningMode"
			state "manual", label: 'Manual', action: "setRobotCleanerCleaningMode"
			state "map", label: 'Map', action: "setRobotCleanerCleaningMode"
            state "stop", label: 'Stop', action: "setRobotCleanerCleaningMode"
			
        }
        standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00a0dc"
			state "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
		}
	}
}

def installed() {
	off()
    setRobotCleanerCleaningMode('stop')
}

def updated() {
	off()
    setRobotCleanerCleaningMode('stop')
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

def resume() {
	sendEvent(name: "robotCleanerCleaningMode", value: device.currentValue("lastMode"))
}

def stop() {
	sendEvent(name: "robotCleanerCleaningMode", value: "stop")
}

def setRobotCleanerCleaningMode(value) {
	sendEvent(name: "robotCleanerCleaningMode", value: value)
    if (value != "stop") {
    	sendEvent(name: "lastMode", value: value)
    }
}
