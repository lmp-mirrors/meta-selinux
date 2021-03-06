#!/bin/sh

/usr/sbin/selinuxenabled 2>/dev/null || exit 0

CHCON=/usr/bin/chcon
MATCHPATHCON=/usr/sbin/matchpathcon
FIXFILES=/sbin/fixfiles
RESTORECON=/sbin/restorecon

for i in ${CHCON} ${MATCHPATHCON} ${FIXFILES} ${RESTORECON} ; do
	test -x $i && continue
	echo "$i is missing in the system."
	echo "Please add \"selinux=0\" in the kernel command line to disable SELinux."
	exit 1
done

check_rootfs()
{
	${CHCON} `${MATCHPATHCON} -n /` / >/dev/null 2>&1 && return 0
	echo ""
	echo "* SELinux requires the root '/' filesystem support extended"
	echo "  filesystem attributes (XATTRs).  It does not appear that this"
	echo "  filesystem has extended attribute support or it is not enabled."
	echo ""
	echo "  - To continue using SELinux you will need to enable extended"
	echo "    attribute support on the root device."
	echo ""
	echo "  - To disable SELinux, please add \"selinux=0\" in the kernel"
	echo "    command line."
	echo ""
	echo "* Halting the system now."
	/sbin/shutdown -f -h now
}

# Because /dev/console is not relabeled by kernel, many commands
# would can not use it, including restorecon.
${CHCON} -t `${MATCHPATHCON} -n /dev/null | cut -d: -f3` /dev/null
${CHCON} -t `${MATCHPATHCON} -n /dev/console | cut -d: -f3` /dev/console


# If /.autorelabel placed, the whole file system should be relabeled
if [ -f /.autorelabel ]; then
	echo "Checking SELinux security contexts:"
	check_rootfs
	echo " * /.autorelabel placed, filesystem will be relabeled..."
	${FIXFILES} -F -f relabel
	/bin/rm -f /.autorelabel
	echo " * Relabel done, rebooting the system."
	/sbin/reboot -f
fi

# If first booting, the security context type of init would be
# "kernel_t", and the whole file system should be relabeled.
if [ "`/usr/bin/secon -t --pid 1`" = "kernel_t" ]; then
	echo "Checking SELinux security contexts:"
	check_rootfs
	echo " * First booting, filesystem will be relabeled..."
	test -x /etc/init.d/auditd && /etc/init.d/auditd start
	/usr/sbin/setenforce 0
	${RESTORECON} -R /
	${RESTORECON} /
	echo " * Relabel done, rebooting the system."
	/sbin/reboot -f
fi

# Now, we should relabel /dev for most services.
${RESTORECON} -R /dev

exit 0
