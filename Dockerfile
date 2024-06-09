FROM openjdk:11-oraclelinux8

# set version number
ARG gradleVersion="7.3.3"
ARG maven3Version="3.8.6"
ARG projectFolder="/root/projects"

# set the directory to execute the command
RUN mkdir /opt/gradle
WORKDIR /opt/gradle

# install common library
RUN microdnf update && microdnf upgrade \
    && microdnf install yum
RUN yum update && yum upgrade \
    && yum install -y /usr/bin/xargs curl wget vim unzip zip git net-tools lsof procps make npm
RUN yum clean all && microdnf clean all

# java
ENV JAVA_HOME /usr/java/openjdk-11

# install gradle
RUN curl -sSOL "https://services.gradle.org/distributions/gradle-${gradleVersion}-bin.zip"
RUN unzip -d /opt/gradle gradle-${gradleVersion}-bin.zip
ENV GRADLE_USER_HOME /opt/gradle/gradle-${gradleVersion}
ENV PATH $GRADLE_USER_HOME/bin:$PATH
RUN gradle -v

# # install Maven
# RUN mkdir -p /opt/maven /opt/maven/ref
# WORKDIR /opt/maven
# RUN curl -sSOL "https://apache.osuosl.org/maven/maven-3/${maven3Version}/binaries/apache-maven-${maven3Version}-bin.tar.gz"
# RUN tar -zxvf apache-maven-${maven3Version}-bin.tar.gz
# RUN rm apache-maven-${maven3Version}-bin.tar.gz
# ENV MAVEN_HOME /opt/maven/apache-maven-${maven3Version}
# ENV PATH $MAVEN_HOME/bin:$PATH
# RUN mvn -version

# install Gauge ( not support aarch64 )
# RUN mkdir /opt/gauge
# WORKDIR /opt/gauge
# RUN curl -SsL https://downloads.gauge.org/stable | sh
# ENV GAUGE_USER_HOME /opt/gauge/
# ENV PATH $GAUGE_USER_HOME/bin:$PATH
# RUN gauge -v

# install git-secrets
WORKDIR /opt/
RUN git clone https://github.com/awslabs/git-secrets
WORKDIR /opt/git-secrets
RUN make install
RUN git-secrets --scan-history

# change dir
RUN mkdir ${projectFolder}
WORKDIR ${projectFolder}

# set m2repo
ENV M2_HOME ${projectFolder}

# set time zone
RUN ln -sf /usr/share/zoneinfo/Asia/Tokyo /etc/localtim

# set bash
RUN touch /root/.bashrc
RUN echo "PS1='[\[\033[01;32m\]\u@Gradle\[\033[00m\]:\[\033[01;34m\]\w\[\033[00m\] \@]\$ '" >> /root/.bashrc
RUN echo "alias ls='ls --color=auto'" >> /root/.bashrc
RUN echo "alias ll='ls -la --color=auto'" >> /root/.bashrc
RUN echo "alias lt='ls -tl --color=auto'" >> /root/.bashrc
RUN echo "alias diff='diff --color=auto'" >> /root/.bashrc
RUN echo "alias ip='ip -color=auto'" >> /root/.bashrc
RUN echo "alias grdl='./gradlew \$@'" >>  /root/.bashrc
RUN echo "export JAVA_HOME=/usr/java/openjdk-11" >>  /root/.bashrc

# set vim
RUN echo '" ---- my config ----' >> /etc/vimrc
RUN echo '" set basic' >> /etc/vimrc
RUN echo "set encoding=utf-8" >> /etc/vimrc
RUN echo "set nobackup" >> /etc/vimrc
RUN echo "set number" >> /etc/vimrc
RUN echo "set title" >> /etc/vimrc
RUN echo "set showcmd" >> /etc/vimrc
RUN echo "set laststatus=2" >> /etc/vimrc
RUN echo "set ruler" >> /etc/vimrc
RUN echo '" set syntax' >> /etc/vimrc
RUN echo "syntax on" >> /etc/vimrc
RUN echo "set showmatch" >> /etc/vimrc
RUN echo '" set search' >> /etc/vimrc
RUN echo "set hlsearch" >> /etc/vimrc
RUN echo "set smartcase" >> /etc/vimrc
RUN echo "set ignorecase" >> /etc/vimrc
RUN echo "set incsearch" >> /etc/vimrc
RUN echo '" set tab' >> /etc/vimrc
RUN echo "set noautoindent" >> /etc/vimrc
RUN echo "set expandtab" >> /etc/vimrc
