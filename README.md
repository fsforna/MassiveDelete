# MassiveDelete
A simple Alfresco massive deletion batch.

The program aims to overcome Alfresco API limits for deleting a large set of nodes in your repository in a simple way. Before you have to prepare the set of nodes to delete by sending a simple SQL query to Alfresco.

SELECT nodes.UUID FROM alf_node nodes, alf_qname names WHERE nodes.type_qname_id=names.id AND names.local_name='PUT_YOUR_TYPE_HERE';
	
# How to build it yourself
Clone the project and configure the batch by editing the `conf.properties` file. Then run `mvn package`.

To execute the program you can use i.e:

	nohup /opt/alfresco/java/bin/java -cp  "MassiveDelete-1.0.jar:$(find lib/ -name "*.jar" | sed -e 's,^,:,g' | tr -d '\n')" MassiveDelete UUID_LIST.txt &
    
# Author
Christian Tiralosi
Francesco Fornasari
