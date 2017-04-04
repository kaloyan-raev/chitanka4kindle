#!/bin/sh
sudo ifconfig enp0s20u2 192.168.2.1
scp installer/chitanka.azw2 root@192.168.2.2:/mnt/us/documents
ssh root@192.168.2.2 'dbus-send --system /default com.lab126.powerd.resuming int32:1ssh'
