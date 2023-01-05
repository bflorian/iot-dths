/**
 *  Simulated Refrigerator
 *
 *  Example composite device handler that simulates a refrigerator with a freezer compartment and a main compartment.
 *  Each of these compartments has its own door, temperature, and temperature setpoint. Each compartment modeled
 *  as a child device of the main refrigerator device so that temperature-based SmartApps can be used with each
 *  compartment
 *
 *  Copyright 2019 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
    definition (name: "Simulated Refrigerator V2", namespace: "smartthings/testing", author: "SmartThings") {
        capability "Contact Sensor"
        capability "Temperature Measurement"
        capability "Health Check"
    }

    tiles(scale: 2) {
		multiAttributeTile(name: "contact", type: "generic", width: 6, height: 4) {
			tileAttribute("device.contact", key: "PRIMARY_CONTROL") {
				attributeState("open", label: '${name}', icon: "st.contact.contact.open", backgroundColor: "#e86d13")
				attributeState("closed", label: '${name}', icon: "st.contact.contact.closed", backgroundColor: "#00A0DC")
			}
		}
        
        childDeviceTile("freezerDoor", "freezer", height: 2, width: 2, childTileName: "contact")
        childDeviceTile("coolerDoor", "cooler", height: 2, width: 2, childTileName: "contact")
        childDeviceTile("cvroomDoor", "cvroom", height: 2, width: 2, childTileName: "contact")

        childDeviceTile("freezerTemperature", "freezer", height: 2, width: 2, childTileName: "temperature")
        childDeviceTile("coolerTemperature", "cooler", height: 2, width: 2, childTileName: "temperature")
        childDeviceTile("cvroomTemperature", "cvroom", height: 2, width: 2, childTileName: "temperature")

        childDeviceTile("freezerSetpoint", "freezer", height: 2, width: 2, childTileName: "setpoint")
        childDeviceTile("coolerSetpoint", "cooler", height: 2, width: 2, childTileName: "setpoint")
        childDeviceTile("cvroomSetpoint", "cvroom", height: 2, width: 2, childTileName: "setpoint")
    }
}

def installed() {
    state.counter = state.counter ? state.counter + 1 : 1
    if (state.counter == 1) {
        addChildDevice(
            "Simulated Refrigerator V2 Compartment",
            "${device.deviceNetworkId}.1",
            null,
            [completedSetup: true, label: "${device.label} (Cooler)", componentName: "cooler", componentLabel: "Cooler"])

        addChildDevice(
            "Simulated Refrigerator V2 Compartment",
            "${device.deviceNetworkId}.2",
            null,
            [completedSetup: true, label: "${device.label} (Freezer)", componentName: "freezer", componentLabel: "Freezer"])

        addChildDevice(
            "Simulated Refrigerator V2 Compartment",
            "${device.deviceNetworkId}.1",
            null,
            [completedSetup: true, label: "${device.label} (CV Room)", componentName: "cvroom", componentLabel: "CV Room"])
    }
    sendEvent(name: "DeviceWatch-DeviceStatus", value: "online")
    sendEvent(name: "healthStatus", value: "online")
    sendEvent(name: "DeviceWatch-Enroll", value: [protocol: "cloud", scheme:"untracked"].encodeAsJson(), displayed: false)
    sendEvent(name: "contact", value: "closed")
    sendEvent(name: "temperature", value: 40)
    initialize()
}

def updated() {
    initialize()
}

def initialize() {
}

def doorOpen(dni) {
    // If any door opens, then the refrigerator is considered to be open
    sendEvent(name: "contact", value: "open")
}

def doorClosed(dni) {
    // Both doors must be closed for the refrigerator to be considered closed
    if (!childDevices.find{it.deviceNetworkId != dni && it.currentValue("contact") == "open"}) {
        sendEvent(name: "contact", value: "closed")
    }
}

def setTemperature(value) {
    sendEvent(name: "temperature", value: value, unit: 'F')
}