import { combineEpics } from 'redux-observable';
import entityEpic from 'app/epic';


export default combineEpics(entityEpic);
