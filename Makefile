#!/usr/bin/make -f

# Adjust according to the number CPU cores to use for parallel build.
# Default: Number of processors in /proc/cpuinfo, if present, or 1.
NR_CPU := $(shell [ -f /proc/cpuinfo ] && grep -c '^processor\s*:' /proc/cpuinfo || echo 1)
BB_NUMBER_THREADS ?= $(NR_CPU)
PARALLEL_MAKE ?= -j $(NR_CPU)

XSUM ?= md5sum
DISTRO_TYPE ?= release
DISTRO ?= openatv

BUILD_DIR = $(CURDIR)/builds/$(DISTRO)/$(DISTRO_TYPE)/$(MACHINE)
TOPDIR = $(BUILD_DIR)
DL_DIR = $(CURDIR)/sources
SSTATE_DIR = $(CURDIR)/builds/$(DISTRO)/sstate-cache
TMPDIR = $(TOPDIR)/tmp
DEPDIR = $(TOPDIR)/.deps
MACHINEBUILD = $(MACHINE)
export MACHINEBUILD

BBLAYERS ?= \
	$(CURDIR)/openembedded-core/meta \
	$(CURDIR)/meta-openembedded/meta-oe \
	$(CURDIR)/meta-openembedded/meta-multimedia \
	$(CURDIR)/meta-openembedded/meta-networking \
	$(CURDIR)/meta-openembedded/meta-filesystems \
	$(CURDIR)/meta-openembedded/meta-python \
	$(CURDIR)/meta-openembedded/meta-webserver \
	$(CURDIR)/meta-oe-alliance/meta-oe \
	$(CURDIR)/meta-dream


CONFFILES = \
	$(TOPDIR)/env.source \
	$(TOPDIR)/conf/$(DISTRO).conf \
	$(TOPDIR)/conf/bblayers.conf \
	$(TOPDIR)/conf/local.conf \
	$(TOPDIR)/conf/site.conf

CONFDEPS = \
	$(DEPDIR)/.env.source.$(BITBAKE_ENV_HASH) \
	$(DEPDIR)/.$(DISTRO).conf.$($(DISTRO)_CONF_HASH) \
	$(DEPDIR)/.bblayers.conf.$(BBLAYERS_CONF_HASH) \
	$(DEPDIR)/.local.conf.$(LOCAL_CONF_HASH)

GIT ?= git
GIT_REMOTE := $(shell $(GIT) remote)
GIT_USER_NAME := $(shell $(GIT) config user.name)
GIT_USER_EMAIL := $(shell $(GIT) config user.email)

hash = $(shell echo $(1) | $(XSUM) | awk '{print $$1}')

.DEFAULT_GOAL := all
all: init
	@echo
	@echo "Openembedded for the oe-alliance environment has been initialized"
	@echo "properly. Now you can start building your image, by doing either:"
	@echo
	@echo "MACHINE=mutant2400 DISTRO=openatv DISTRO_TYPE=release make image"
	@echo "	or"
	@echo "cd $(BUILD_DIR) ; source env.source ; bitbake $(DISTRO)-image"
	@echo

$(BBLAYERS):
	[ -d $@ ] || $(MAKE) $(MFLAGS) update

initialize: init

init: $(BBLAYERS) $(CONFFILES)

image: init
	@. $(TOPDIR)/env.source && cd $(TOPDIR) && bitbake $(DISTRO)-image

clean:
	@. $(TOPDIR)/env.source && cd $(TOPDIR) && echo -n -e "Performing a clean \e[95mPlease wait... " && bitbake -qqq -c clean $(DISTRO)-image && echo -n -e "\e[93mClean completed.\e[0m"

update:
	@echo 'Updating Git repositories...'
	@HASH=`$(XSUM) $(MAKEFILE_LIST)`; \
	if [ -n "$(GIT_REMOTE)" ]; then \
		$(GIT) pull --ff-only || $(GIT) pull --rebase; \
	fi; \
	if [ "$$HASH" != "`$(XSUM) $(MAKEFILE_LIST)`" ]; then \
		echo 'Makefile changed. Restarting...'; \
		$(MAKE) $(MFLAGS) --no-print-directory $(MAKECMDGOALS); \
	else \
		$(GIT) submodule sync && \
		$(GIT) submodule update --init && \
		cd meta-oe-alliance  && \
		if [ -n "$(GIT_REMOTE)" ]; then \
			$(GIT) submodule sync && \
			$(GIT) submodule update --init; \
		fi; \
		echo "The oe-alliance is now up-to-date." ; \
		cd .. ; \
	fi

.PHONY: all image init initialize update usage machinebuild

BITBAKE_ENV_HASH := $(call hash, \
	'BITBAKE_ENV_VERSION = "0"' \
	'CURDIR = "$(CURDIR)"' \
	'MACHINEBUILD2 = "${MACHINEBUILD}"' \
	)

$(TOPDIR)/env.source: $(DEPDIR)/.env.source.$(BITBAKE_ENV_HASH)
	@echo 'Generating $@'
	@echo 'export BB_ENV_EXTRAWHITE="MACHINE DISTRO MACHINEBUILD BB_SRCREV_POLICY BB_NO_NETWORK"' > $@
	@echo 'export MACHINE=$(MACHINE)' >> $@
	@echo 'export DISTRO=$(DISTRO)' >> $@
	@echo 'export MACHINEBUILD=$(MACHINEBUILD)' >> $@
	@echo 'export PATH=$(CURDIR)/openembedded-core/scripts:$(CURDIR)/bitbake/bin:$${PATH}' >> $@
	@echo 'export BB_SRCREV_POLICY="cache"' >> $@


$(DISTRO)_CONF_HASH := $(call hash, \
	'$(DISTRO)_CONF_VERSION = "1"' \
	'CURDIR = "$(CURDIR)"' \
	'BB_NUMBER_THREADS = "$(BB_NUMBER_THREADS)"' \
	'PARALLEL_MAKE = "$(PARALLEL_MAKE)"' \
	'DL_DIR = "$(DL_DIR)"' \
	'SSTATE_DIR = "$(SSTATE_DIR)"' \
	'TMPDIR = "$(TMPDIR)"' \
	)

$(TOPDIR)/conf/$(DISTRO).conf: $(DEPDIR)/.$(DISTRO).conf.$($(DISTRO)_CONF_HASH)
	@echo 'Generating $@'
	@test -d $(@D) || mkdir -p $(@D)
	@echo 'DISTRO_TYPE = "$(DISTRO_TYPE)"' >> $@
	@echo 'SSTATE_DIR = "$(SSTATE_DIR)"' >> $@
	@echo 'TMPDIR = "$(TMPDIR)"' >> $@
	@echo 'BB_GENERATE_MIRROR_TARBALLS = "1"' >> $@
	@echo 'BBINCLUDELOGS = "yes"' >> $@
	@echo 'CONF_VERSION = "1"' >> $@
	@echo 'EXTRA_IMAGE_FEATURES = "debug-tweaks"' >> $@
	@echo 'USER_CLASSES = "buildstats"' >> $@
	@echo '#PRSERV_HOST = "localhost:0"' >> $@


LOCAL_CONF_HASH := $(call hash, \
	'LOCAL_CONF_VERSION = "0"' \
	'CURDIR = "$(CURDIR)"' \
	'TOPDIR = "$(TOPDIR)"' \
	)

$(TOPDIR)/conf/local.conf: $(DEPDIR)/.local.conf.$(LOCAL_CONF_HASH)
	@echo 'Generating $@'
	@test -d $(@D) || mkdir -p $(@D)
	@echo 'TOPDIR = "$(TOPDIR)"' > $@
	@echo 'require $(TOPDIR)/conf/$(DISTRO).conf' >> $@

$(TOPDIR)/conf/site.conf: $(CURDIR)/site.conf
	@ln -s ../../../../../site.conf $@

$(CURDIR)/site.conf:
	@echo 'SCONF_VERSION = "1"' >> $@
	@echo 'BB_NUMBER_THREADS = "$(BB_NUMBER_THREADS)"' >> $@
	@echo 'PARALLEL_MAKE = "$(PARALLEL_MAKE)"' >> $@
	@echo 'BUILD_OPTIMIZATION = "-O2 -pipe"' >> $@
	@echo 'DL_DIR = "$(DL_DIR)"' >> $@
	@echo 'INHERIT += "rm_work"' >> $@
	@echo 'BB_GIT_SHALLOW_DEPTH = "1"' >> $@
	@echo 'BB_GIT_SHALLOW = "1"' >> $@

BBLAYERS_CONF_HASH := $(call hash, \
	'BBLAYERS_CONF_VERSION = "5"' \
	'CURDIR = "$(CURDIR)"' \
	'BBLAYERS = "$(BBLAYERS)"' \
	)

$(TOPDIR)/conf/bblayers.conf: $(DEPDIR)/.bblayers.conf.$(BBLAYERS_CONF_HASH)
	@echo 'Generating $@'
	@test -d $(@D) || mkdir -p $(@D)
	@echo 'LCONF_VERSION = "4"' > $@
	@echo 'BBFILES = ""' >> $@
	@echo 'BBLAYERS = "$(BBLAYERS)"' >> $@

$(CONFDEPS):
	@test -d $(@D) || mkdir -p $(@D)
	@$(RM) $(basename $@).*
	@touch $@
