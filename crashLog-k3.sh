#!/bin/sh
sudo ifconfig enp0s20u2 192.168.2.1
scp root@192.168.2.2:/mnt/us/developer/Читанка/work/crash.log .
