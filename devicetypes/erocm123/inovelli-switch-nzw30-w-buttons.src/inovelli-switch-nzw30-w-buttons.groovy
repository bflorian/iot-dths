/**
 *  Inovelli Switch NZW30/NZW30T w/Scene
 *  Author: Eric Maycock (erocm123)
 *  Date: 2018-12-04
 *
 *  Copyright 2018 Eric Maycock
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
 *  2018-12-04: Added option to "Disable Remote Control" and to send button events 1,pushed / 1,held for on / off.
 *
 *  2018-08-03: Added the ability to change the label on scenes.
 *
 *  2018-06-20: Modified tile layout. Update firmware version reporting. Bug Fix.
 *
 *  2018-06-08: Remove communication method check from updated().
 *
 *  2018-04-11: No longer deleting child devices when user toggles the option off. SmartThings was throwing errors.
 *              User will have to manually delete them.
 *
 *  2018-03-08: Added support for local protection to disable local control. Requires firmware 1.03+.
 *              Also merging handler from NZW30T as they are identical other than the LED indicator.
 *              Child device creation option added for local control setting. Child device must be installed:
 *              https://github.com/erocm123/SmartThingsPublic/blob/master/devicetypes/erocm123/switch-level-child-device.src
 *
 *  2018-02-26: Added support for Z-Wave Association Tool SmartApp. Associations require firmware 1.02+.
 *              https://github.com/erocm123/SmartThingsPublic/tree/master/smartapps/erocm123/z-waveat
 */

metadata {
    definition (name: "Inovelli Switch NZW30 w/Buttons", namespace: "erocm123", author: "Eric Maycock", vid: "generic-switch") {
        capability "Switch"
        capability "Refresh"
        capability "Polling"
        //capability "Health Check"
        capability "Button"
        capability "Configuration"

        attribute "lastActivity", "String"
        attribute "lastEvent", "String"
        attribute "firmware", "String"

        fingerprint mfr: "015D", prod: "B111", model: "1E1C", deviceJoinName: "Inovelli Switch"
        fingerprint mfr: "015D", prod: "1E00", model: "1E00", deviceJoinName: "Inovelli Switch"
        fingerprint mfr: "0312", prod: "1E00", model: "1E00", deviceJoinName: "Inovelli Switch"
        fingerprint mfr: "0312", prod: "1E02", model: "1E02", deviceJoinName: "Inovelli Switch" // Toggle version NZW30T
        fingerprint deviceId: "0x1001", inClusters: "0x5E,0x86,0x72,0x5A,0x85,0x59,0x73,0x25,0x27,0x70,0x5B,0x75,0x22,0x8E,0x55,0x6C,0x7A"
    }

    simulator {
    }

    preferences {
        input "ledIndicator", "enum", title: "LED Indicator\n\nTurn LED indicator on when light is: (Paddle Switch Only)\n", description: "Tap to set", required: false, options:[["1": "On"], ["0": "Off"], ["2": "Disable"], ["3": "Always On"]], defaultValue: "0"
        input "invert", "enum", title: "Invert Switch\n\nInvert on & off on the physical switch", description: "Tap to set", required: false, options:[["0": "No"], ["1": "Yes"]], defaultValue: "0"
    }

    tiles {
        multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
            tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "turningOn"
                attributeState "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState: "turningOff"
                attributeState "turningOff", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "turningOn"
                attributeState "turningOn", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState: "turningOff"
            }
        }

        valueTile("icon", "device.firmware", decoration: "flat", width: 2, height: 1) {
            state "default", label: 'Firmware\n${currentValue}'
        }

        standardTile("refresh", "device.switch", decoration: "flat", width: 1, height: 1) {
            state "default", label: "", action: "refresh.refresh", icon: "st.secondary.refresh"
        }
    }
}

private channelNumber(String dni) {
    dni.split("-ep")[-1] as Integer
}

private sendAlert(data) {
    sendEvent(
            descriptionText: data.message,
            eventType: "ALERT",
            name: "failedOperation",
            value: "failed",
            displayed: true,
    )
}

void childSetLevel(String dni, value) {
    def valueaux = value as Integer
    def level = Math.max(Math.min(valueaux, 99), 0)
    def cmds = []
    switch (channelNumber(dni)) {
        case 101:
            cmds << new physicalgraph.device.HubAction(command(zwave.protectionV2.protectionSet(localProtectionState : level > 0 ? 2 : 0, rfProtectionState: 0) ))
            cmds << new physicalgraph.device.HubAction(command(zwave.protectionV2.protectionGet() ))
            break
    }
    sendHubCommand(cmds, 1000)
}


def installed() {
    log.debug "installed()"
    refresh()
}

def configure() {
    log.debug "configure()"
    def cmds = initialize()
    commands(cmds)
}

def updated() {
    if (!state.lastRan || now() >= state.lastRan + 2000) {
        log.debug "updated()"
        state.lastRan = now()
        def cmds = initialize()
        response(commands(cmds))
    } else {
        log.debug "updated() ran within the last 2 seconds. Skipping execution."
    }
}

def initialize() {
    sendEvent(name: "checkInterval", value: 3 * 60 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])
	sendEvent(name: "supportedButtonValues", value: [
    			"up_2x",
                "up_3x",
                "up_4x",
                "up_5x",
                "up_hold",
                "down_2x",
                "down_3x",
                "down_4x",
                "down_5x",
                "down_hold"].encodeAsJSON(), displayed: false)
                
    def cmds = []
    cmds << zwave.versionV1.versionGet()
    cmds << zwave.configurationV1.configurationSet(scaledConfigurationValue: ledIndicator!=null? ledIndicator.toInteger() : 0, parameterNumber: 3, size: 1)
    cmds << zwave.configurationV1.configurationGet(parameterNumber: 3)
    cmds << zwave.configurationV1.configurationSet(scaledConfigurationValue: invert!=null? invert.toInteger() : 0, parameterNumber: 4, size: 1)
    cmds << zwave.configurationV1.configurationGet(parameterNumber: 4)
    cmds << zwave.configurationV1.configurationSet(scaledConfigurationValue: autoOff!=null? autoOff.toInteger() : 0, parameterNumber: 5, size: 2)
    cmds << zwave.configurationV1.configurationGet(parameterNumber: 5)

    return cmds
}

def parse(description) {
    def result = null
    if (description.startsWith("Err 106")) {
        state.sec = 0
        result = createEvent(descriptionText: description, isStateChange: true)
    } else if (description != "updated") {
        def cmd = zwave.parse(description, [0x20: 1, 0x25: 1, 0x70: 1, 0x98: 1])
        if (cmd) {
            result = zwaveEvent(cmd)
            //log.debug("'$description' parsed to $result")
        } else {
            log.debug("Couldn't zwave.parse '$description'")
        }
    }
    def now
    if(location.timeZone)
        now = new Date().format("yyyy MMM dd EEE h:mm:ss a", location.timeZone)
    else
        now = new Date().format("yyyy MMM dd EEE h:mm:ss a")
    sendEvent(name: "lastActivity", value: now, displayed:false)
    result
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
    createEvent(name: "switch", value: cmd.value ? "on" : "off", type: "physical")
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
    createEvent(name: "switch", value: cmd.value ? "on" : "off", type: "physical")
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
    createEvent(name: "switch", value: cmd.value ? "on" : "off", type: "digital")
}

def zwaveEvent(physicalgraph.zwave.commands.securityv1.SecurityMessageEncapsulation cmd) {
    def encapsulatedCommand = cmd.encapsulatedCommand([0x20: 1, 0x25: 1])
    if (encapsulatedCommand) {
        state.sec = 1
        zwaveEvent(encapsulatedCommand)
    }
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
    switch (cmd.keyAttributes) {
        case 1:
            if (cmd.sceneNumber == 2) {
                createEvent(name: "button", value: "up_hold", descriptionText: '{{ device.displayName}} was held up', isStateChange: true, data: [buttonNumber: 1])
            }
            else {
                createEvent(name: "button", value: "down_hold", descriptionText: '{{ device.displayName}} was held down', isStateChange: true, data: [buttonNumber: 1])
            }
            break
        default:
            def count = cmd.keyAttributes - 1
            if (cmd.sceneNumber == 2) {
                createEvent(name: "button", value: "up_${count}x", descriptionText: "{{ device.displayName}} was tapped up ${count} times", isStateChange: true, data: [buttonNumber: 1])
            }
            else {
                createEvent(name: "button", value: "down_${count}x", descriptionText: "{{ device.displayName}} was tapped down ${count} times", isStateChange: true, data: [buttonNumber: 1])
            }
            break
    }
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv1.ConfigurationReport cmd) {
    log.debug "${device.displayName} parameter '${cmd.parameterNumber}' with a byte size of '${cmd.size}' is set to '${cmd2Integer(cmd.configurationValue)}'"
}

def cmd2Integer(array) {
    switch(array.size()) {
        case 1:
            array[0]
            break
        case 2:
            ((array[0] & 0xFF) << 8) | (array[1] & 0xFF)
            break
        case 3:
            ((array[0] & 0xFF) << 16) | ((array[1] & 0xFF) << 8) | (array[2] & 0xFF)
            break
        case 4:
            ((array[0] & 0xFF) << 24) | ((array[1] & 0xFF) << 16) | ((array[2] & 0xFF) << 8) | (array[3] & 0xFF)
            break
    }
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
    log.debug "Unhandled: $cmd"
    null
}

def on() {
	log.trace "on()"
    commands([
        zwave.basicV1.basicSet(value: 0xFF),
        zwave.basicV1.basicGet()
    ])
}

def off() {
    log.trace "off()"
    commands([
        zwave.basicV1.basicSet(value: 0x00),
        zwave.basicV1.basicGet()
    ])
}


def ping() {
    refresh()
}

def poll() {
    refresh()
}

def refresh() {
    commands(zwave.basicV1.basicGet())
}

private command(physicalgraph.zwave.Command cmd) {
    if (state.sec) {
        zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
    } else {
        cmd.format()
    }
}

private commands(commands, delay=500) {
    def result = delayBetween(commands.collect{ command(it) }, delay)
    log.trace "commands = ${commands}"
    return result
}

def zwaveEvent(physicalgraph.zwave.commands.versionv1.VersionReport cmd) {
    log.debug cmd
    if(cmd.applicationVersion && cmd.applicationSubVersion) {
        String firmware = "${cmd.applicationVersion}.${cmd.applicationSubVersion.toString().padLeft(2,'0')}"
        state.needfwUpdate = "false"
        createEvent(name: "firmware", value: firmware)
    }
}

def zwaveEvent(physicalgraph.zwave.commands.protectionv2.ProtectionReport cmd) {
    log.debug cmd
    def integerValue = cmd.localProtectionState
    def children = childDevices
    def childDevice = children.find{it.deviceNetworkId.endsWith("ep101")}
    if (childDevice) {
        childDevice.sendEvent(name: "switch", value: integerValue > 0 ? "on" : "off")
    }
}