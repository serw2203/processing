import { combineEpics } from 'redux-observable';
import { mapTo, filter } from 'rxjs/operators';

const pingEpic = (action$ : any, state$) => action$.pipe(
    filter(action => action.type === 'PING'),
    mapTo({ type: 'PONG' })
  );

export default combineEpics(pingEpic);
