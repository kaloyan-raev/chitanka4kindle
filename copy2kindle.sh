#!/bin/sh
sudo ifconfig usb0 192.168.2.1
scp target/chitanka.azw2 root@192.168.2.2:/mnt/us/documents
