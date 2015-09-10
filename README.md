# geocrowd-pricing
Geocrowd Pricing Strategies

# Case Study
We would like to examine how reward correlates with various costs in order to incentivize people to accept/perform a spatial crowdsourcing (SC) task.
There are campaigns, in which lots of people will go out and take pictures. Assuming people to upload pictures by providing reward. The idea is to define some certain task locations in some areas, each task with a particular reward to motivate people taking pictures. Given a limited budget over the course of the event, we are interested in how to dynamically determine rewards for each task so that we achieve our campaign goal, e.g., the total number of collected pictures is maximized and uniform distributed across locations.

# Guideline
In the following each pricing strategy should be designed such that it builds on the previous strategy, i.e., such that strategy i subsumes strategy i-1.  In other words, strategy i-1 is a special case of strategy i. The purpose of this generalization is to easily deploy the algorithm in real-world.

# Strategy 1
The baseline strategy is to give a fixed reward to each task.

# Strategy 2
The first strategy is to balance the rewards allocated to tasks based on the number of collected pictures per task. For example, if there were many pictures collected for a particular task, we would reduce its reward while increasing rewards for the other tasks with fewer pictures. Toward that end, we want to propose a reward function that decays with the number of collected pictures. Our hypothesis is that this reward function would result in maximum coverage of the tasks. 

# Strategy 3
The second strategy is to consider the current distribution of user locations instead of picture count distribution. Thus, we need to design the second reward function based on user distribution. Intuitively, this function needs to take the user-task distance into consideration, e.g., if time required for all users to travel to a particular task location passes its deadline (i.e., the end time of the event), we adjust the reward of that task to zero. 

# Strategy 4
The third strategy is to include the task deadline. Intuitively, we may want to increase the reward (within limited budget left) for tasks as time reaches their deadline.
