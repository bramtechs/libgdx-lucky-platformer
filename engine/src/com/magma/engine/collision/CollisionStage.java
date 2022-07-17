package com.magma.engine.collision;

import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.magma.engine.utils.MagmaMath;

public class CollisionStage extends Stage {

	private final HashSet<Actor> senders; // a huge set of all the 'Triggerable' annotated actors or actors implementing
											// TriggerListener that need to interact with each other
	private final HashSet<Actor> receivers;
	private final Queue<Actor> removeQueue; 

	private boolean printDebug;

	public CollisionStage(Viewport viewport, Batch batch) {
		super(viewport, batch);
		senders = new HashSet<Actor>();
		receivers = new HashSet<Actor>();
		removeQueue = new Queue<Actor>();
		Timer.schedule(new Task() {
			@Override
			public void run() {
				Gdx.app.log("CollisionStage", "senders: " + senders.size() + " listeners: " + receivers.size());
			}
		}, 2f, 10f);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		// remove dead objects
		while (removeQueue.notEmpty()) {
			Actor dead = removeQueue.removeFirst();
			senders.remove(dead);
			receivers.remove(dead);
		}

		// check collision between senders and receivers
		for (Actor sender : senders) {
			Shape2D senderShape = MagmaMath.extractShape(sender);
			// check if dead
			if (!sender.hasParent()) {
				removeQueue.addLast(sender);
				continue;
			}
			for (Actor receiver : receivers) {
				// check if dead
				if (!receiver.hasParent()) {
					removeQueue.addLast(receiver);
					continue;
				}
				Shape2D receiverShape = MagmaMath.extractShape(receiver);
				if (MagmaMath.shapesOverlap(senderShape, receiverShape)) {
					if (printDebug) {
						System.out.println(sender.getClass().getSimpleName() + " --> " + receiver.toString());
					}
					((TriggerListener) receiver).onTriggered(sender);
				}
			}
		}
	}


	@Override
	public void addActor(Actor actor) {
		if (actor instanceof Group) {
			Group group = (Group) actor;
			for (Actor a : group.getChildren()) {
				registerActor(actor);
			}
		}
		registerActor(actor);
		super.addActor(actor);
	}

	public void registerActor(Actor actor) {
		if (actor instanceof Triggered) {
			senders.add(actor);
		} else if (actor instanceof TriggerListener) {
			receivers.add(actor);
		}
	}

    public void unregisterActor(Actor actor){
        senders.remove(actor);
        receivers.remove(actor);
    }

	public void enablePrint() {
		printDebug = true;
	}

	@Override
	public String toString() {
		return "senders: " + senders.size() + " listeners: " + receivers.size();
	}
}
