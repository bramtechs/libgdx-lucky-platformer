package com.magma.engine.chars;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Queue;
import com.magma.engine.debug.MagmaLogger;

public class CharacterParty extends Action {

    private final Character leader;
    private final Queue<CharacterMover> members;
    private final Queue<GridPoint2> trail;

    public CharacterParty(Character leader) {
        this.leader = leader;
        this.trail = new Queue<GridPoint2>();
        this.members = new Queue<CharacterMover>();

        leader.addAction(this);
    }

    public void join(Character newChar) {
        // inject characterMover
        CharacterMover mover = new CharacterMover(newChar.getEntity());
        newChar.addAction(mover);
        members.addLast(mover);
        Gdx.app.log("CharacterParty", newChar.getClass().getSimpleName() + " joined the party!");
    }

    public void joinFirst(Character newChar) {
        // inject characterMover
        CharacterMover mover = new CharacterMover(newChar.getEntity());
        newChar.addAction(mover);
        members.addFirst(mover);
        Gdx.app.log("CharacterParty", newChar.getClass().getSimpleName() + " joined the party in front!");
    }

    public void leave(CharacterMover mover) {
        MagmaLogger.log(this, mover.getActor().getClass().getSimpleName() + " left the party!");
        members.removeValue(mover, false);
        mover.stop();
    }

    public void leave(Character curChar) {
        Iterator<CharacterMover> it = members.iterator();
        while (it.hasNext()) {
            CharacterMover mover = it.next();
            if (mover.getActor() == curChar) {
                break;
            }
        }
    }

    public void disband() {
        Iterator<CharacterMover> it = members.iterator();
        while (it.hasNext()) {
            CharacterMover mover = it.next();
            leave(mover);
        }
    }

    @Override
    public boolean act(float delta) {
        // check if moved
        int x = (int) (getActor().getX());
        int y = (int) (getActor().getY());

        while (trail.size < members.size) {
            trail.addLast(new GridPoint2());
        }

        GridPoint2 head = trail.first();
        if (head.x != x || head.y != y) {
            // player moved
            trail.addFirst(new GridPoint2(x, y));

            // move all members
            for (int i = 0; i < trail.size - 1; i++) {
                GridPoint2 pos = trail.get(i);
                members.get(i).target(pos);
            }

        }

        while (trail.size > members.size) {
            trail.removeLast();
        }

        return false;
    }

    @Override
    public String toString() {
        String text = "CharacterParty: ";
        if (trail.isEmpty()) {
            text += "(no pos)";
            return text;
        }

        for (int i = 0; i < trail.size; i++) {
            GridPoint2 pos = trail.get(i);
            text += " > " + pos.toString();
            if (i < members.size) {
                text += " " + members.get(i).getActor().getClass().getSimpleName();
            }
        }
        return text;
    }
}
