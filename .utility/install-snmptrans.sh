workingdir=$(pwd)

cd $HOME
git clone https://github.com/btravers/snmptrans.git
cd snmptrans
mvn install

cd $workingdir