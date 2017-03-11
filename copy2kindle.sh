#!/bin/sh
sudo ifconfig enp0s20u2 192.168.15.201
scp target/chitanka.azw2 root@192.168.15.244:/mnt/us/documents
ssh root@192.168.15.244 'dbus-send --system /default com.lab126.powerd.resuming int32:1ssh'
